package com.huskydreaming.settlements.inventories.actions;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.settlements.inventories.base.InventoryAction;
import com.huskydreaming.settlements.inventories.base.InventoryActionType;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.TrustService;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class UnTrustInventoryAction implements InventoryAction {

    private final String settlement;
    private final OfflinePlayer offlinePlayer;
    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;
    private final TrustService trustService;

    public UnTrustInventoryAction(HuskyPlugin plugin, String settlement, OfflinePlayer offlinePlayer) {
        this.plugin = plugin;
        this.settlement = settlement;
        this.offlinePlayer = offlinePlayer;
        this.inventoryService = plugin.provide(InventoryService.class);
        this.trustService = plugin.provide(TrustService.class);
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.UN_TRUST;
    }

    @Override
    public void run(Player player) {
        trustService.unTrust(offlinePlayer, settlement);
        inventoryService.getMembersInventory(plugin, player).open(player);
    }
}