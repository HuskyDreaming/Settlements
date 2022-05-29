package com.huskydreaming.settlements.inventories.settlement;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SettlementsInventory extends InventoryPageProvider<Settlement> {

    public SettlementsInventory(int rows, Settlements settlements) {
        super(null, rows, settlements.getSettlementManager().getSettlements().toArray(new Settlement[0]));
    }

    @Override
    public ItemStack construct(int index, Settlement settlement) {
        int citizens = settlement.getCitizens().length;
        int land = settlement.getLands().size();

        return ItemBuilder.create()
                .setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + index + ". " + settlement.getName())
                .setMaterial(Material.WRITABLE_BOOK)
                .setAmount(settlement.getCitizens().length)
                .setLore(
                        ChatColor.YELLOW + settlement.getDescription(),
                        "",
                        ChatColor.GRAY + "Land: " + ChatColor.GREEN + land + "/" + settlement.getMaxLand(),
                        ChatColor.GRAY + "Citizens: " + ChatColor.GREEN + citizens + "/" + settlement.getMaxCitizens()
                ).build();
    }

    @Override
    public void run(InventoryClickEvent event, Settlement settlement, InventoryContents contents) {

    }
}
