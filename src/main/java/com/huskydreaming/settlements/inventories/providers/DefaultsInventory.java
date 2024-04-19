package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.enumeration.types.SettlementDefaultType;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.storage.types.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DefaultsInventory extends InventoryPageProvider<SettlementDefaultType> {

    private final Config config;
    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;

    public DefaultsInventory(HuskyPlugin plugin, int rows) {
        super(rows);
        this.plugin = plugin;

        config = plugin.provide(ConfigService.class).getConfig();
        inventoryService = plugin.provide(InventoryService.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        contents.set(0, 0, InventoryItem.back(player, inventoryService.getAdminInventory(player, plugin)));
    }

    @Override
    public ItemStack construct(Player player, int i, SettlementDefaultType type) {
        String title = Util.capitalize(type.name().replace("_", " "));
        return ItemBuilder
                .create()
                .setDisplayName(Menu.ADMIN_DEFAULT_TITLE.parameterize(title))
                .setLore(Menu.ADMIN_DEFAULT_LORE.parameterizeList(config.getSettlementDefault(type)))
                .setMaterial(Material.PAPER)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, SettlementDefaultType type, InventoryContents contents) {
        if(event.getWhoClicked() instanceof Player player) {
            int value = config.getSettlementDefault(type);
            if (event.isRightClick() && value > 1) {
                value -= 1;
            }
            if (event.isLeftClick()) {
                value += 1;
            }

            config.setSettlementDefault(type, value);
            contents.inventory().open(player);
        }
    }
}
