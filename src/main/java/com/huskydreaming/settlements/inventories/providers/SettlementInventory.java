package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryModuleProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.inventories.actions.DisbandInventoryAction;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.enumeration.locale.Menu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SettlementInventory extends InventoryModuleProvider {

    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final SettlementService settlementService;

    public SettlementInventory(HuskyPlugin plugin) {
        this.plugin = plugin;
        this.inventoryService = plugin.provide(InventoryService.class);
        this.memberService = plugin.provide(MemberService.class);
        this.settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        if (!memberService.hasSettlement(player)) return;

        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);

        if (!settlement.isOwner(player)) return;

        contents.set(Math.min(getRows() + 2, 5) - 1, 8, ClickableItem.of(disbandItem(), this::disbandClick));
    }

    private ItemStack disbandItem() {
        return ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_DISBAND_TITLE.parse())
                .setLore(Menu.SETTLEMENT_DISBAND_LORE.parseList())
                .setMaterial(Material.TNT_MINECART)
                .build();
    }

    private void disbandClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (!memberService.hasSettlement(player)) return;

            Member member = memberService.getMember(player);
            Settlement settlement = settlementService.getSettlement(member);

            if (!settlement.isOwner(player)) return;

            inventoryService.addAction(player, new DisbandInventoryAction(plugin, settlement));
            inventoryService.getConfirmationInventory(plugin, player).open(player);
        }
    }
}