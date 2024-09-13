package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.database.dao.SettlementDao;
import com.huskydreaming.settlements.database.entities.Claim;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Settlement;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;

public interface SettlementService extends Service {

    Settlement createSettlement(Player player, String name);

    void addSettlement(Settlement settlement);

    void disbandSettlement(Settlement settlement);

    boolean isSettlement(String name);

    Settlement getSettlement(String string);

    Settlement getSettlement(Claim claim);

    Settlement getSettlement(Member member);

    Settlement getSettlement(long id);

    Map<Long, Settlement> getSettlements();

    Set<Settlement> getSettlements(Set<Long> settlementIds);

    SettlementDao getDao();
}