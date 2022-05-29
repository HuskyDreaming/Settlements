package com.huskydreaming.settlements.inventories;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.managers.SettlementManager;
import fr.minuskube.inv.content.InventoryProvider;

public abstract class InventorySettlementProvider implements InventoryProvider {

    protected final SettlementManager settlementManager;

    protected InventorySettlementProvider() {
        this.settlementManager = Settlements.getInstance().getSettlementManager();
    }

    public SettlementManager getSettlementManager() {
        return settlementManager;
    }
}
