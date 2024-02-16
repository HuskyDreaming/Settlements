package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.List;

@Command(label = CommandLabel.ADMIN, requiresPermissions = true)
public class AdminCommand implements CommandInterface {

    private final ClaimService claimService;
    private final MemberService memberService;
    private final SettlementService settlementService;

    public AdminCommand() {
        claimService = ServiceProvider.Provide(ClaimService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 1) {
            sendHelp(player);
        } else if (strings.length == 2) {
            String string = strings[1].toUpperCase();
            if(CommandLabel.contains(string)) {
                switch (CommandLabel.valueOf(string)) {
                    case UNCLAIM -> sendUnClaim(player);
                    case HELP -> sendHelp(player);
                }
                return;
            }
            sendUnknown(player, string);
        }
        else if (strings.length == 3) {
            String string = strings[1].toUpperCase();
            if(CommandLabel.contains(string)) {
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
        if(settlementService.isSettlement(string)) {
            Settlement settlement = settlementService.getSettlement(string);

            boolean isAdjacent = false;
            for(Chunk c : claimService.getChunks(settlement)) {
                if (Remote.areAdjacentChunks(chunk, c)) isAdjacent = true;
            }

            if(isAdjacent) {
                String x = String.valueOf(chunk.getX());
                String z = String.valueOf(chunk.getZ());

                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_CLAIM, x, z));
                claimService.setClaim(chunk, settlement);
            } else {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_ADJACENT));
            }

        } else {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_NULL, string));
        }
    }

    private void sendUnClaim(Player player) {
        Chunk chunk = player.getLocation().getChunk();

        if(!claimService.isClaim(chunk)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_NOT_CLAIMED));
            return;
        }

        Settlement settlement = settlementService.getSettlement(claimService.getClaim(chunk));
        if(claimService.getChunks(settlement).size() == 1) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_UNCLAIM_ONE));
        } else {
            claimService.removeClaim(chunk);
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_UNCLAIM));
        }
    }

    private void sendDisband(Player player, String string) {
        if(!settlementService.isSettlement(string)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_NULL, string));
            return;
        }

        Settlement settlement = settlementService.getSettlement(string);

        claimService.clean(settlement);
        memberService.clean(settlement);
        settlementService.disbandSettlement(settlement);

        player.sendMessage(Remote.prefix(Locale.SETTLEMENT_DISBAND));
    }

    private void sendUnknown(Player player, String string) {
        player.sendMessage(Remote.prefix(Locale.UNKNOWN_SUBCOMMAND, string));
    }

    private void sendHelp(Player player) {
        List<String> strings = Locale.ADMIN_HELP.parseList();
        if(strings == null) return;

        strings.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).forEach(player::sendMessage);
    }
}
