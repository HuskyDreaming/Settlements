package com.huskydreaming.settlements.services;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import org.bukkit.entity.Player;

import java.util.Set;

public interface SettlementService extends ServiceInterface {

    Settlement createSettlement(Player player, String name);

    void disbandSettlement(Settlement settlement);

    boolean isSettlement(String name);

    Settlement getSettlement(String string);

    Set<Settlement> getSettlements();
}
