package com.huskydreaming.settlements.services.base;

import com.huskydreaming.settlements.SettlementPlugin;

public interface ServiceInterface {

    void serialize(SettlementPlugin plugin);
    void deserialize(SettlementPlugin plugin);
}
