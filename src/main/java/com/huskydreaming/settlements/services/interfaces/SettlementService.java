package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import org.bukkit.entity.Player;

import java.util.Map;

public interface SettlementService extends ServiceInterface {

    Settlement createSettlement(Player player, String name);

    void disbandSettlement(String name);

    boolean isSettlement(String name);

    Settlement getSettlement(String string);

    Map<String, Settlement> getSettlements();

    Map<String, Integer> getDefaults();
}