package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Claim;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.List;

@Command(label = CommandLabel.ADMIN)
public class AdminCommand implements CommandInterface {

    private final ClaimService claimService;
    private final MemberService memberService;
    private final SettlementService settlementService;

    public AdminCommand(SettlementPlugin plugin) {
        claimService = plugin.provide(ClaimService.class);
        memberService = plugin.provide(MemberService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 1) {
            sendHelp(player);
        } else if (strings.length == 2) {
            String string = strings[1].toUpperCase();
            if (CommandLabel.contains(string)) {
                switch (CommandLabel.valueOf(string)) {
                    case UNCLAIM -> sendUnClaim(player);
                    case HELP -> sendHelp(player);
                }
                return;
            }
            sendUnknown(player, string);
        } else if (strings.length == 3) {
            String string = strings[1].toUpperCase();
            if (CommandLabel.contains(string)) {
                switch (CommandLabel.valueOf(string)) {
                    case CLAIM -> sendClaim(player, strings[2]);
                    case DISBAND -> sendDisband(player, strings[2]);
                }
                return;
            }
            sendUnknown(player, string);
        }
    }

    private void sendClaim(Player player, String string) {
        Chunk chunk = player.getLocation().getChunk();
        if (settlementService.isSettlement(string)) {

            boolean isAdjacent = false;
            for (Claim claim : claimService.getClaims(string)) {
                if (Remote.areAdjacentChunks(chunk, claim.toChunk())) isAdjacent = true;
            }

            if (isAdjacent) {
                String x = String.valueOf(chunk.getX());
                String z = String.valueOf(chunk.getZ());

                player.sendMessage(Locale.SETTLEMENT_LAND_CLAIM.prefix(x, z));
                claimService.setClaim(chunk, string);
            } else {
                player.sendMessage(Locale.SETTLEMENT_LAND_ADJACENT.prefix());
            }

        } else {
            player.sendMessage(Locale.SETTLEMENT_NULL.prefix(string));
        }
    }

    private void sendUnClaim(Player player) {
        Chunk chunk = player.getLocation().getChunk();

        if (!claimService.isClaim(chunk)) {
            player.sendMessage(Locale.SETTLEMENT_LAND_NOT_CLAIMED.prefix());
            return;
        }

        String claim = claimService.getClaim(chunk);
        if (claimService.getClaims(claim).size() == 1) {
            player.sendMessage(Locale.SETTLEMENT_LAND_UNCLAIM_ONE.prefix());
        } else {
            claimService.removeClaim(chunk);
            player.sendMessage(Locale.SETTLEMENT_LAND_UNCLAIM.prefix());
        }
    }

    private void sendDisband(Player player, String string) {
        if (!settlementService.isSettlement(string)) {
            player.sendMessage(Locale.SETTLEMENT_NULL.prefix(string));
            return;
        }

        claimService.clean(string);
        memberService.clean(string);
        settlementService.disbandSettlement(string);

        player.sendMessage(Locale.SETTLEMENT_DISBAND.prefix());
    }

    private void sendUnknown(Player player, String string) {
        player.sendMessage(Locale.UNKNOWN_SUBCOMMAND.prefix(string));
    }

    private void sendHelp(Player player) {
        List<String> strings = Locale.ADMIN_HELP.parseList();
        if (strings == null) return;

        strings.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).forEach(player::sendMessage);
    }
}