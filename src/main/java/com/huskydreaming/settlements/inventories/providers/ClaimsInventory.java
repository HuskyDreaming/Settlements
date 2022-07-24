package com.huskydreaming.settlements.inventories.providers;

import com.google.inject.Inject;
import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.InventoryService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ClaimsInventory extends InventoryPageProvider<String> {

    @Inject
    private InventoryService inventoryService;

    public ClaimsInventory(Settlement settlement, int rows, String[] chunks) {
        super(settlement, rows, chunks);
        this.smartInventory =  inventoryService.getSettlementInventory(settlement);
    }

    @Override
    public ItemStack construct(int index, String string) {
        String[] data = string.split(":");
        return ItemBuilder.create()
                .setDisplayName(ChatColor.GREEN + "" + data[0] + ", " + data[1])
                .setLore(
                        ChatColor.GRAY + "Left-Click  - Teleport",
                        ChatColor.GRAY + "Right-Click - Set Owner"
                )
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

                World world = Bukkit.getWorld(data[3]);
                if (world != null) {
                    Chunk chunk = world.getChunkAt(dataX, dataZ);

                    int x = (chunk.getX() << 4) + 8;
                    int z = (chunk.getZ() << 4) + 8;
                    int y = world.getHighestBlockYAt(x, z, HeightMap.MOTION_BLOCKING_NO_LEAVES);

                    Location location = new Location(world, x, y + 1, z);
                    player.teleport(location);
                    player.sendMessage("You have teleported to: " + x + ", " + z);
                }
            }
        }
    }
}
