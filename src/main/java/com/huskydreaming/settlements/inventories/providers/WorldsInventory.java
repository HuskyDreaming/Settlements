package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.storage.types.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WorldsInventory extends InventoryPageProvider<String> {

    private final HuskyPlugin plugin;
    private final ConfigService configService;
    private final InventoryService inventoryService;

    public WorldsInventory(HuskyPlugin plugin, int rows) {
        super(rows);
        this.plugin = plugin;

        this.configService = plugin.provide(ConfigService.class);
        this.inventoryService = plugin.provide(InventoryService.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        contents.set(0, 0, InventoryItem.back(player, inventoryService.getAdminInventory(player, plugin)));
    }

    @Override
    public ItemStack construct(Player player, int i, String s) {
        World world = Bukkit.getWorld(s);
        if (world == null) return null;

        Material material = null;
        boolean enabled = !configService.getConfig().containsDisableWorld(world);
        switch (world.getEnvironment()) {
            case NETHER -> material = Material.NETHERRACK;
            case THE_END -> material = Material.END_STONE;
            case NORMAL -> material = Material.GRASS_BLOCK;
            case CUSTOM -> material = Material.DIAMOND_BLOCK;
        }

        if (world.getEnvironment() == World.Environment.NORMAL) {
            return InventoryItem.of(enabled, material, s, world.getDifficulty().name());
        } else {
            return ItemBuilder.create()
                    .setDisplayName(Menu.ADMIN_DISABLED_WORLD_TITLE.parameterize(s))
                    .setLore(Menu.ADMIN_DISABLED_WORLD_LORE.parseList())
                    .setMaterial(material)
                    .build();
        }
    }

    @Override
    public void run(InventoryClickEvent event, String s, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            World world = Bukkit.getWorld(s);
            if (world == null) return;

            Config config = configService.getConfig();
            if (world.getEnvironment() == World.Environment.NORMAL) {
                if (config.containsDisableWorld(world)) {
                    config.removeDisabledWorld(world);
                } else {
                    config.addDisabledWorld(world);
                }
                contents.inventory().open(player);
            }
        }
    }
}