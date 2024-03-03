package com.huskydreaming.settlements.registries;

import com.huskydreaming.settlements.SettlementPlugin;

public interface Registry {

    default void register(SettlementPlugin plugin) {

    }
    default void unregister(SettlementPlugin plugin) {

    }

}
