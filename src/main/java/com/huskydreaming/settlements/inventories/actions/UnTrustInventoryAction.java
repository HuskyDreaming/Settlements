package com.huskydreaming.settlements.inventories.actions;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.enumeration.filters.MemberFilter;
import com.huskydreaming.settlements.inventories.base.InventoryAction;
import com.huskydreaming.settlements.inventories.base.InventoryActionType;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.TrustService;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class UnTrustInventoryAction implements InventoryAction {

    private final long settlementId;
    private final OfflinePlayer offlinePlayer;
    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;
    private final TrustService trustService;

    public UnTrustInventoryAction(HuskyPlugin plugin, Settlement settlement, OfflinePlayer offlinePlayer) {
        this.plugin = plugin;
        this.settlementId = settlement.getId();
        this.offlinePlayer = offlinePlayer;
        this.inventoryService = plugin.provide(InventoryService.class);
        this.trustService = plugin.provide(TrustService.class);
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.UN_TRUST;
    }

    @Override
    public void onAccept(Player player) {
        trustService.unTrust(offlinePlayer, settlementId);
        inventoryService.getMembersInventory(plugin, player, MemberFilter.ALL).open(player);
    }

    @Override
    public void onDeny(Player player) {
        inventoryService.getMainInventory(plugin, player).open(player);
    }
}