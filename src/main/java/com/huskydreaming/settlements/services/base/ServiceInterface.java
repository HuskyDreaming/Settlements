package com.huskydreaming.settlements.services.base;

import com.huskydreaming.settlements.SettlementPlugin;

public interface ServiceInterface {

    default void serialize(SettlementPlugin plugin) {

    }

    default void deserialize(SettlementPlugin plugin) {

    }
}