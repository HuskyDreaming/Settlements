package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.types.Menu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ConfirmationInventory implements InventoryProvider {

    private final InventoryService inventoryService;

    public ConfirmationInventory(HuskyPlugin plugin) {
        this.inventoryService = plugin.provide(InventoryService.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(InventoryItem.border());

        contents.set(1, 3, accept(player));
        contents.set(1, 5, deny(player));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ClickableItem accept(Player player) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.CONFIRMATION_YES_TITLE.parse())
                .setMaterial(Material.GREEN_TERRACOTTA)
                .build();

        return ClickableItem.of(itemStack, e -> inventoryService.acceptAction(player));
    }

    private ClickableItem deny(Player player) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.CONFIRMATION_NO_TITLE.parse())
                .setMaterial(Material.RED_TERRACOTTA)
                .build();

        return ClickableItem.of(itemStack, e -> inventoryService.denyAction(player));
    }
}