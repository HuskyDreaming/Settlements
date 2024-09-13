package com.huskydreaming.settlements.inventories.modules.general;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.enumeration.locale.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RoleModule implements InventoryModule {

    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;
    private final MemberService memberService;

    public RoleModule(HuskyPlugin plugin) {
        this.plugin = plugin;
        this.inventoryService = plugin.provide(InventoryService.class);
        this.memberService = plugin.provide(MemberService.class);
    }

    @Override
    public ItemStack itemStack(Player player) {
        return ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_ROLES_TITLE.parse())
                .setLore(Menu.SETTLEMENT_ROLES_LORE.parseList())
                .setMaterial(Material.ANVIL)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, InventoryContents contents) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!memberService.hasSettlement(player)) return;
        inventoryService.getRolesInventory(plugin, player).open(player);
    }
}