package com.huskydreaming.settlements.inventories;

import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class InventoryItem {

    public static ClickableItem of(boolean permission, ItemStack itemStack, Consumer<InventoryClickEvent> consumer) {
        if(permission) return ClickableItem.of(itemStack, consumer);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta != null) {
            return ClickableItem.empty(ItemBuilder.create()
                    .setDisplayName(ChatColor.RED + itemMeta.getDisplayName())
                    .setLore(ChatColor.GRAY + "No permissions.")
                    .build());
        }
        return ClickableItem.empty(new ItemStack(Material.GRAY_DYE));
    }

    public static ClickableItem border() {
        return ClickableItem.empty(ItemBuilder.create()
                .setDisplayName(ChatColor.RESET + "")
                .setMaterial(Material.BLACK_STAINED_GLASS_PANE)
                .build());
    }

    public static ClickableItem next(Player player, InventoryContents contents) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(ChatColor.WHITE + "Next")
                .setLore(ChatColor.GRAY + "Click for next page.")
                .setMaterial(Material.ARROW)
                .build();

        return ClickableItem.of(itemStack, event -> contents.inventory().open(player, contents.pagination().next().getPage()));
    }

    public static ClickableItem back(Player player, SmartInventory smartInventory) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(ChatColor.WHITE + "Back")
                .setLore(ChatColor.GRAY + "Click to go back.")
                .setMaterial(Material.ARROW)
                .build();

        return ClickableItem.of(itemStack, event -> smartInventory.open(player));
    }

    public static ClickableItem previous(Player player, InventoryContents contents) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(ChatColor.WHITE + "Previous")
                .setLore(ChatColor.GRAY + "Click for previous page.")
                .setMaterial(Material.ARROW)
                .build();

        return ClickableItem.of(itemStack, e -> contents.inventory().open(player, contents.pagination().previous().getPage()));
    }
}
