package com.huskydreaming.settlements.inventories.base;

import org.bukkit.entity.Player;

public interface InventoryAction {

    InventoryActionType getType();

    void run(Player player);
}
