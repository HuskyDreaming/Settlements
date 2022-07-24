package com.huskydreaming.settlements.inventories.providers;

import com.google.inject.Inject;
import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Citizen;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.InventoryService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CitizensInventory extends InventoryPageProvider<Citizen> {

    @Inject
    private InventoryService inventoryService;

    public CitizensInventory(Settlement settlement, int rows, Citizen[] citizens) {
        super(settlement, rows, citizens);
        this.smartInventory = inventoryService.getSettlementInventory(settlement);
    }

    @Override
    public ItemStack construct(int index, Citizen citizen) {
        //String status = offlinePlayer.isOnline() ? ChatColor.GREEN + "Online" : ChatColor.RED + "Offline";
        return ItemBuilder.create()
                .setDisplayName(ChatColor.YELLOW + "" + index + ". ")
                .setLore(
                        ChatColor.GRAY + "" + ChatColor.DARK_GRAY + " | ",
                        "",
                        ChatColor.GRAY + "Click to edit player."
                )
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, Citizen Citizen, InventoryContents contents) {
        if(event.getWhoClicked() instanceof Player player) {
           // inventoryService.getCitizenInventory(settlement).open(player);
        }
    }
}
