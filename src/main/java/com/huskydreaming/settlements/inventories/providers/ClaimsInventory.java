package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Claim;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.storage.enumerations.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ClaimsInventory extends InventoryPageProvider<Claim> {

    public ClaimsInventory(SettlementPlugin plugin, String name, int rows, Claim[] claims) {
        super(rows, claims);
        InventoryService inventoryService = plugin.provide(InventoryService.class);
        this.smartInventory = inventoryService.getSettlementInventory(plugin, name);
    }

    @Override
    public ItemStack construct(int index, Claim claim) {
        return ItemBuilder.create()
                .setDisplayName(Menu.CLAIMS_TITLE.parameterize(claim.getX(), claim.getZ()))
                .setLore(Menu.CLAIMS_LORE.parseList())
                .setMaterial(Material.GRASS_BLOCK)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, Claim claim, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if (event.isLeftClick()) {
                Chunk chunk = claim.toChunk();
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