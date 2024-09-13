package com.huskydreaming.settlements.inventories.modules.admin;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.enumeration.types.ConfigType;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.enumeration.locale.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AdminDefaultsModule implements InventoryModule {

    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;

    public AdminDefaultsModule(HuskyPlugin plugin) {
        this.plugin = plugin;
        this.inventoryService = plugin.provide(InventoryService.class);
    }

    @Override
    public ItemStack itemStack(Player player) {
        ConfigType configType = ConfigType.SETTLEMENT;
        return ItemBuilder.create()
                .setDisplayName(Menu.ADMIN_DEFAULTS_TITLE.parse())
                .setLore(Menu.ADMIN_DEFAULTS_LORE.parameterizeList(configType.getDescription()))
                .setMaterial(Material.WRITABLE_BOOK)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            inventoryService.getSettlementDefaultsInventory(plugin).open(player);
        }
    }
}