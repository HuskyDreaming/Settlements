package com.huskydreaming.settlements.inventories;

import com.huskydreaming.settlements.persistence.Settlement;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public abstract class InventoryPageProvider<E> implements InventoryProvider {

    protected SmartInventory smartInventory;
    protected final Settlement settlement;
    private final int rows;
    private final E[] array;

    public InventoryPageProvider(Settlement settlement, int rows, E[] array) {
        this.settlement = settlement;
        this.rows = rows;
        this.array = array;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(InventoryItem.border());
        if(smartInventory != null) contents.set(0, 0, InventoryItem.back(player, smartInventory));

        ClickableItem[] clickableItems = new ClickableItem[array.length];
        for(int i = 0; i < clickableItems.length; i++) {
            AtomicInteger atomicInteger = new AtomicInteger(i);
            ItemStack itemStack = construct(i + 1, array[atomicInteger.get()]);
            Consumer<InventoryClickEvent> consumer = e -> run(e, array[atomicInteger.get()], contents);
            clickableItems[i] = ClickableItem.of(itemStack, consumer);
        }

        setupPagination(player, contents, clickableItems);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    public abstract ItemStack construct(int index, E e);

    public abstract void run(InventoryClickEvent event, E e, InventoryContents contents);

    private void setupPagination(Player player, InventoryContents contents, ClickableItem[] clickableItems) {
        Pagination pagination = contents.pagination();

        pagination.setItems(clickableItems);
        pagination.setItemsPerPage(Math.min(rows * 9, 3 * 9));

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0));

        if(!pagination.isLast() && !pagination.isFirst()) {
            contents.set(4, 1, InventoryItem.previous(player, contents));
            contents.set(4, 7, InventoryItem.next(player, contents));
        }

        if(!pagination.isLast()) {
            contents.set(4, 7, InventoryItem.next(player, contents));
        }

        if(!pagination.isFirst()) {
            contents.set(4, 1, InventoryItem.previous(player, contents));
        }
    }
}
