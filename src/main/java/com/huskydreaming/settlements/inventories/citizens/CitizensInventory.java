package com.huskydreaming.settlements.inventories.citizens;

import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.inventories.InventorySupplier;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.utilities.Chat;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CitizensInventory extends InventoryPageProvider<OfflinePlayer> {

    public CitizensInventory(Settlement settlement, int rows) {
        super(settlement, rows, settlement.getCitizens());
        this.smartInventory = InventorySupplier.getSettlementInventory(settlement);
    }

    @Override
    public ItemStack construct(int index, OfflinePlayer offlinePlayer) {
        String status = offlinePlayer.isOnline() ? ChatColor.GREEN + "Online" : ChatColor.RED + "Offline";
        return ItemBuilder.create()
                .setDisplayName(ChatColor.YELLOW + "" + index + ". " + offlinePlayer.getName())
                .setLore(
                        ChatColor.GRAY + settlement.getRole(offlinePlayer).getName() + ChatColor.DARK_GRAY + " | " +  status,
                        "",
                        ChatColor.GRAY + "Click to edit player."
                )
                .buildPlayer(offlinePlayer);
    }

    @Override
    public void run(InventoryClickEvent event, OfflinePlayer offlinePlayer, InventoryContents contents) {
        InventorySupplier.getCitizenInventory(settlement, offlinePlayer).open((Player) event.getWhoClicked());
    }
}
