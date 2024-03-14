package com.huskydreaming.settlements.inventories;

import com.huskydreaming.settlements.utilities.ItemBuilder;
import com.huskydreaming.settlements.storage.enumerations.Menu;
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
        if (permission) return ClickableItem.of(itemStack, consumer);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            return ClickableItem.empty(ItemBuilder.create()
                    .setDisplayName(Menu.GENERAL_NO_PERMISSIONS_TITLE.parameterize(itemMeta.getDisplayName()))
                    .setLore(Menu.GENERAL_NO_PERMISSIONS_LORE.parseList())
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
                .setDisplayName(Menu.GENERAL_NEXT_TITLE.parse())
                .setLore(Menu.GENERAL_NEXT_LORE.parseList())
                .setMaterial(Material.ARROW)
                .build();

        return ClickableItem.of(itemStack, event -> contents.inventory().open(player, contents.pagination().next().getPage()));
    }

    public static ClickableItem back(Player player, SmartInventory smartInventory) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.GENERAL_BACK_TITLE.parse())
                .setLore(Menu.GENERAL_BACK_LORE.parseList())
                .setMaterial(Material.ARROW)
                .build();

        return ClickableItem.of(itemStack, event -> smartInventory.open(player));
    }

    public static ClickableItem previous(Player player, InventoryContents contents) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.GENERAL_PREVIOUS_TITLE.parse())
                .setLore(Menu.GENERAL_PREVIOUS_LORE.parseList())
                .setMaterial(Material.ARROW)
                .build();

        return ClickableItem.of(itemStack, e -> contents.inventory().open(player, contents.pagination().previous().getPage()));
    }
}