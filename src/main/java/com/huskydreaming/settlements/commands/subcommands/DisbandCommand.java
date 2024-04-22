package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.inventories.actions.DisbandInventoryAction;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.types.Message;
import org.bukkit.entity.Player;

@CommandAnnotation(label = CommandLabel.DISBAND)
public class DisbandCommand implements PlayerCommandProvider {

    private final HuskyPlugin plugin;
    private final MemberService memberService;
    private final SettlementService settlementService;

    private final InventoryService inventoryService;

    public DisbandCommand(HuskyPlugin plugin) {
        this.plugin = plugin;
        inventoryService = plugin.provide(InventoryService.class);
        memberService = plugin.provide(MemberService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Message.PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        if (!settlement.isOwner(player)) {
            player.sendMessage(Message.OWNER_NOT.prefix());
            return;
        }

        inventoryService.addAction(player, new DisbandInventoryAction(plugin, member.getSettlement()));
        inventoryService.getConfirmationInventory(plugin, player).open(player);
    }
}