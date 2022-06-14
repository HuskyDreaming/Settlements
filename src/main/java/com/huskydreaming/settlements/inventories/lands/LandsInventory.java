package com.huskydreaming.settlements.inventories.lands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.lands.Land;
import com.huskydreaming.settlements.services.InventoryService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class LandsInventory extends InventoryPageProvider<Land> {

    @Inject
    private InventoryService inventoryService;

    public LandsInventory(Settlement settlement, int rows) {
        super(settlement, rows, settlement.getLands().toArray(new Land[0]));
        this.smartInventory = inventoryService.getSettlementInventory(settlement);
    }

    @Override
    public ItemStack construct(int index, Land land) {
        return ItemBuilder.create()
                .setDisplayName(ChatColor.GREEN + "" + land.getX() + ", " + land.getZ())
                .setLore(
                        ChatColor.GRAY + "Left-Click  - Teleport",
                        ChatColor.GRAY + "Right-Click - Set Owner"
                )
                .setMaterial(Material.GRASS_BLOCK)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, Land land, InventoryContents contents) {
        if(event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();

            if(event.isLeftClick()) {

                World world = Bukkit.getWorld(settlement.getWorld());
                if (world != null) {
                    Chunk chunk = world.getChunkAt(land.getX(), land.getZ());

                    int x = (chunk.getX() << 4) + 8;
                    int z = (chunk.getZ() << 4) + 8;
                    int y = world.getHighestBlockYAt(x, z, HeightMap.MOTION_BLOCKING_NO_LEAVES);

                    Location location = new Location(world, x, y + 1, z);
                    player.teleport(location);
                    player.sendMessage("You have teleported to: " + x + ", " + z);
                }
            } else if(event.isRightClick()) {
                inventoryService.getLandsOwnerInventory(settlement, land).open(player);
            }
        }
    }
}
