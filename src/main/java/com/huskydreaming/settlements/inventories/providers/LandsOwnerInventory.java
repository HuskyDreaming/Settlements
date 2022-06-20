package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.lands.Land;
import com.huskydreaming.settlements.services.InventoryService;
import com.huskydreaming.settlements.services.base.ServiceRegistry;
import com.huskydreaming.settlements.services.base.ServiceType;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class LandsOwnerInventory extends InventoryPageProvider<OfflinePlayer> {
    private final Land land;

    public LandsOwnerInventory(Settlement settlement, int rows, Land land) {
        super(settlement, rows, settlement.getCitizens());

        InventoryService inventoryService = (InventoryService) ServiceRegistry.getService(ServiceType.INVENTORY);

        this.smartInventory = inventoryService.getLandsInventory(settlement);
        this.land = land;
    }

    @Override
    public ItemStack construct(int index, OfflinePlayer offlinePlayer) {
        UUID owner = land.getUniqueId();
        boolean isOwner = owner != null && offlinePlayer.getUniqueId().equals(owner);
        String lore = isOwner ? "I am the owner!" : "Click to set as owner.";

        return ItemBuilder.create()
                .setDisplayName(ChatColor.YELLOW + offlinePlayer.getName())
                .setLore(ChatColor.GRAY + lore)
                .setEnchanted(isOwner)
                .buildPlayer(offlinePlayer);
    }

    @Override
    public void run(InventoryClickEvent event, OfflinePlayer offlinePlayer, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if(land.getUniqueId() != null && land.getUniqueId().equals(offlinePlayer.getUniqueId())) return;

            land.setUniqueId(offlinePlayer.getUniqueId());
            contents.inventory().open(player);
        }
    }
}
