package com.huskydreaming.settlements.services;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceInterface;

import java.util.Set;

public interface SettlementService extends ServiceInterface {

    void createSettlement(String name);

    void disbandSettlement(Settlement settlement);

    boolean isSettlement(String name);

    Settlement getSettlement(String string);

    Set<Settlement> getSettlements();
}
