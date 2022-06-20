package com.huskydreaming.settlements.services;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.Service;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import com.huskydreaming.settlements.services.base.ServiceType;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Set;

@Service(type = ServiceType.SETTLEMENT)
public interface SettlementService extends ServiceInterface {

    void createSettlement(Player player, String name);

    void disbandSettlement(Settlement settlement);

    boolean hasSettlement(Player player);

    boolean isSettlement(String name);

    boolean isSettlement(Chunk chunk);

    Settlement getSettlement(Chunk chunk);

    Settlement getSettlement(String string);

    Settlement getSettlement(Player player);

    Set<Settlement> getSettlements();
}
