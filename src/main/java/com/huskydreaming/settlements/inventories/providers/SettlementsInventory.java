package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.ClaimService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SettlementsInventory extends InventoryPageProvider<Settlement> {

    public SettlementsInventory(int rows, Settlement[] settlements) {
        super(null, rows, settlements);
    }

    @Override
    public ItemStack construct(int index, Settlement settlement) {
        //int citizens = settlement.getCitizens().length;
        //int land = settlement.getLands().size();

        return ItemBuilder.create()
                .setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + index + ". " + settlement.getName())
                .setMaterial(Material.WRITABLE_BOOK)
                .setLore(
                        ChatColor.YELLOW + settlement.getDescription(),
                        "",
                        ChatColor.GRAY + "Land: " + ChatColor.GREEN + 0 + "/" + settlement.getMaxLand(),
                        ChatColor.GRAY + "Citizens: " + ChatColor.GREEN + 0 + "/" + settlement.getMaxCitizens()
                ).build();
    }

    @Override
    public void run(InventoryClickEvent event, Settlement settlement, InventoryContents contents) {

    }
}
