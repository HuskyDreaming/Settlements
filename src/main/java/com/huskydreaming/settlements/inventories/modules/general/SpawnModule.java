package com.huskydreaming.settlements.inventories.modules.general;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.Permission;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.types.Locale;
import com.huskydreaming.settlements.storage.types.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SpawnModule implements InventoryModule {

    private final MemberService memberService;
    private final SettlementService settlementService;

    public SpawnModule(HuskyPlugin plugin) {
        this.memberService = plugin.provide(MemberService.class);
        this.settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public ItemStack itemStack(Player player) {
        return ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_SPAWN_TITLE.parse())
                .setLore(Menu.SETTLEMENT_SPAWN_LORE.parseList())
                .setMaterial(Material.ENDER_PEARL)
                .build();
    }

    @Override
    public Permission getPermission() {
        return RolePermission.EDIT_SPAWN;
    }

    @Override
    public void run(InventoryClickEvent event, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if (memberService.hasSettlement(player)) {
                Member member = memberService.getCitizen(player);
                Settlement settlement = settlementService.getSettlement(member.getSettlement());
                settlement.setLocation(player.getLocation());
                player.closeInventory();
                player.sendMessage(Locale.SETTLEMENT_SET_SPAWN.prefix());
            } else {
                player.closeInventory();
            }
        }
    }
}