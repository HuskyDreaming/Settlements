package com.huskydreaming.settlements.inventories.base;

import org.bukkit.entity.Player;

public interface InventoryAction {

    InventoryActionType getType();

    void onAccept(Player player);

    void onDeny(Player player);
}
