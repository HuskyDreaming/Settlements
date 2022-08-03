package com.huskydreaming.settlements.inventories.providers;

import com.google.inject.Inject;
import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Citizen;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.CitizenService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CitizensInventory extends InventoryPageProvider<OfflinePlayer> {

    @Inject
    private CitizenService citizenService;

    public CitizensInventory(Settlement settlement, int rows, OfflinePlayer[] offlinePlayers) {
        super(settlement, rows, offlinePlayers);
    }

    @Override
    public ItemStack construct(int index, OfflinePlayer offlinePlayer) {
        Citizen citizen = citizenService.getCitizen(offlinePlayer);
        String status = offlinePlayer.isOnline() ? ChatColor.GREEN + "Online" : ChatColor.RED + "Offline";
        return ItemBuilder.create()
                .setDisplayName(ChatColor.YELLOW + "" + index + ". " + offlinePlayer.getName())
                .setLore(
                        ChatColor.GRAY + citizen.getRole() + ChatColor.DARK_GRAY + " | " + status,
                        "",
                        ChatColor.GRAY + "Click to edit player."
                )
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, OfflinePlayer offlinePlayer, InventoryContents contents) {
        if(event.getWhoClicked() instanceof Player player) {
           // inventoryService.getCitizenInventory(settlement).open(player);
        }
    }
}
