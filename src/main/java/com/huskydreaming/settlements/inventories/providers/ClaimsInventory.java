package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.data.ChunkData;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.storage.types.Locale;
import com.huskydreaming.settlements.storage.types.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ClaimsInventory extends InventoryPageProvider<ChunkData> {

    private final boolean teleportation;
    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;

    public ClaimsInventory(HuskyPlugin plugin, int rows, ChunkData[] chunks) {
        super(rows, chunks);
        this.plugin = plugin;

        ConfigService configService = plugin.provide(ConfigService.class);
        this.teleportation = configService.getConfig().isTeleportation();
        this.inventoryService = plugin.provide(InventoryService.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        contents.set(0, 0, InventoryItem.back(player, inventoryService.getMainInventory(plugin, player)));
    }

    @Override
    public ItemStack construct(Player player, int i, ChunkData data) {
        ItemBuilder builder = ItemBuilder.create()
                .setDisplayName(Menu.CLAIMS_TITLE.parameterize(data.getX(), data.getZ()))
                .setMaterial(Material.GRASS_BLOCK);

        if (teleportation) builder.setLore(Menu.CLAIMS_LORE.parseList());
        return builder.build();
    }

    @Override
    public void run(InventoryClickEvent event, ChunkData data, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if (event.isLeftClick() && teleportation) {
                Chunk chunk = data.toChunk();
                World world = chunk.getWorld();
                int x = (chunk.getX() << 4) + 8;
                int z = (chunk.getZ() << 4) + 8;
                int y = world.getHighestBlockYAt(x, z, HeightMap.MOTION_BLOCKING_NO_LEAVES);

                Location location = new Location(world, x, y + 1, z);
                player.teleport(location);
                player.sendMessage(Locale.SETTLEMENT_TELEPORT.prefix(x, z));
            }
        }
    }
}