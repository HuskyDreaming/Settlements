package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.storage.enumerations.Menu;
import com.huskydreaming.settlements.utilities.Remote;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ClaimsInventory extends InventoryPageProvider<String> {

    public ClaimsInventory(SettlementPlugin plugin, String name, int rows, String[] chunks) {
        super(rows, chunks);
        InventoryService inventoryService = plugin.provide(InventoryService.class);
        this.smartInventory =  inventoryService.getSettlementInventory(plugin, name);
    }

    @Override
    public ItemStack construct(int index, String string) {
        String[] data = string.split(":");
        return ItemBuilder.create()
                .setDisplayName(Remote.parameterize(Menu.CLAIMS_TITLE, data[0], data[1]))
                .setLore(Menu.CLAIMS_LORE.parseList())
                .setMaterial(Material.GRASS_BLOCK)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, String string, InventoryContents contents) {
        if(event.getWhoClicked() instanceof Player player) {
            if(event.isLeftClick()) {
                String[] data = string.split(":");
                int dataX = Integer.parseInt(data[0]);
                int dataZ = Integer.parseInt(data[1]);

                World world = Bukkit.getWorld(data[2]);
                if (world != null) {
                    Chunk chunk = world.getChunkAt(dataX, dataZ);

                    int x = (chunk.getX() << 4) + 8;
                    int z = (chunk.getZ() << 4) + 8;
                    int y = world.getHighestBlockYAt(x, z, HeightMap.MOTION_BLOCKING_NO_LEAVES);

                    Location location = new Location(world, x, y + 1, z);
                    player.teleport(location);
                    player.sendMessage(Remote.prefix(Locale.SETTLEMENT_TELEPORT, x, z));
                }
            }
        }
    }
}