package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.database.dao.ClaimDao;
import com.huskydreaming.settlements.database.entities.Claim;
import com.huskydreaming.settlements.database.entities.Settlement;
import org.bukkit.Chunk;

import java.util.LinkedHashMap;
import java.util.Set;

public interface ClaimService extends Service {

    Claim createClaim(Chunk chunk);

    void addClaim(Claim claim);

    void addClaim(Settlement settlement, Chunk chunk, Runnable runnable);


    void removeClaim(Claim claim, Runnable runnable);

    void clean(Settlement settlement);

    boolean isClaim(Chunk chunk);

    Claim getClaim(Settlement settlement, Chunk chunk);

    Claim getClaim(Chunk chunk);

    Set<Claim> getClaims(long settlementId);

    Set<Claim> getClaims(Settlement settlement);

    int getCount();

    LinkedHashMap<Long, Long> getTop(int limit);

    boolean isAdjacent(Settlement settlement, Chunk chunk);

    boolean isAdjacentToOtherClaim(Settlement settlement, Chunk chunk);

    boolean isAdjacentToExistingClaim(Chunk chunk);

    ClaimDao getDao();
}