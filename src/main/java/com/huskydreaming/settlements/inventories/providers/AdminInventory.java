package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AdminInventory extends InventoryPageProvider<InventoryModule> {

    public AdminInventory(int rows) {
        super(rows);
    }

    @Override
    public ItemStack construct(Player player, int i, InventoryModule module) {
        return module.itemStack(player);
    }

    @Override
    public void run(InventoryClickEvent event, InventoryModule module, InventoryContents contents) {
        module.run(event, contents);
    }
}