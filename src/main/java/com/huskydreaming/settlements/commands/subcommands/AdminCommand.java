package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.database.entities.Claim;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.List;


@CommandAnnotation(label = CommandLabel.ADMIN, arguments = " [claim|disband|help|unclaim]")
public class AdminCommand implements PlayerCommandProvider {

    private final HuskyPlugin plugin;
    private final ClaimService claimService;
    private final FlagService flagService;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;
    private final TrustService trustService;

    public AdminCommand(HuskyPlugin plugin) {
        this.plugin = plugin;

        claimService = plugin.provide(ClaimService.class);
        inventoryService = plugin.provide(InventoryService.class);
        flagService = plugin.provide(FlagService.class);
        roleService = plugin.provide(RoleService.class);
        memberService = plugin.provide(MemberService.class);
        settlementService = plugin.provide(SettlementService.class);
        trustService = plugin.provide(TrustService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length == 1) {
            inventoryService.getAdminInventory(player, plugin).open(player);
        } else if (strings.length == 2) {
            String string = strings[1].toUpperCase();

            if (string.equalsIgnoreCase(CommandLabel.UN_CLAIM)) {
                sendUnClaim(player);
            } else if (string.equalsIgnoreCase(CommandLabel.HELP)) {
                sendHelp(player);
            } else {
                sendUnknown(player, string);
            }
        } else if (strings.length == 3) {
            String string = strings[1].toUpperCase();
            if (string.equalsIgnoreCase(CommandLabel.CLAIM)) {
                sendClaim(player, strings[2]);
            } else if (string.equalsIgnoreCase(CommandLabel.DISBAND)) {
                sendDisband(player, strings[2]);
            } else {
                sendUnknown(player, string);
            }
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (strings.length == 2)
            return List.of(CommandLabel.CLAIM, CommandLabel.DISBAND, CommandLabel.HELP, CommandLabel.UN_CLAIM);
        return List.of();
    }

    private void sendClaim(Player player, String string) {
        Chunk chunk = player.getLocation().getChunk();
        boolean isAdjacent = false;
        Settlement settlement = settlementService.getSettlement(string);
        if(settlement == null) {
            player.sendMessage(Message.NULL.prefix(string));
            return;
        }
        for (Claim claim : claimService.getClaims(settlement)) {
            if (Util.areAdjacentChunks(chunk, claim.toChunk())) isAdjacent = true;
        }

        if (isAdjacent) {
            String x = String.valueOf(chunk.getX());
            String z = String.valueOf(chunk.getZ());

            claimService.addClaim(settlement, chunk, () -> player.sendMessage(Message.LAND_CLAIM.prefix(x, z)));
        } else {
            player.sendMessage(Message.LAND_ADJACENT.prefix());
        }
    }

    private void sendUnClaim(Player player) {
        Chunk chunk = player.getLocation().getChunk();

        if (!claimService.isClaim(chunk)) {
            player.sendMessage(Message.LAND_NOT_CLAIMED.prefix());
            return;
        }

        Claim claim = claimService.getClaim(chunk);
        if (claimService.getClaims(claim.getSettlementId()).size() == 1) {
            player.sendMessage(Message.LAND_UN_CLAIM_ONE.prefix());
        } else {
            claimService.removeClaim(claim, () -> player.sendMessage(Message.LAND_UN_CLAIM.prefix()));
        }
    }

    private void sendDisband(Player player, String string) {
        if (!settlementService.isSettlement(string)) {
            player.sendMessage(Message.NULL.prefix(string));
            return;
        }
        Settlement settlement = settlementService.getSettlement(string);
        if(settlement == null) {
            player.sendMessage(Message.NULL.prefix(string));
            return;
        }

        claimService.clean(settlement);
        memberService.clean(settlement);
        flagService.clean(settlement);
        roleService.clean(settlement);
        trustService.clean(settlement);
        settlementService.disbandSettlement(settlement);

        player.sendMessage(Message.DISBAND_YES.prefix());
    }

    private void sendUnknown(Player player, String string) {
        player.sendMessage(Message.GENERAL_UNKNOWN_SUBCOMMAND.prefix(string));
    }

    private void sendHelp(Player player) {
        List<String> strings = Message.GENERAL_ADMIN_HELP.parseList();
        if (strings == null) return;

        strings.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).forEach(player::sendMessage);
    }
}