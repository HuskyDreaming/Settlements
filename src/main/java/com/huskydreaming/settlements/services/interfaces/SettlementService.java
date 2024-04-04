package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import org.bukkit.entity.Player;

import java.util.Map;

public interface SettlementService extends Service {

    Settlement createSettlement(Player player, String name);

    void disbandSettlement(String name);

    boolean isSettlement(String name);

    Settlement getSettlement(String string);

    Map<String, Settlement> getSettlements();
}