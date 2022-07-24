package com.huskydreaming.settlements.services;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import org.bukkit.Chunk;

import java.util.Collection;

public interface ClaimService extends ServiceInterface {

    void setClaim(String string, Chunk chunk);
    void removeClaim(Chunk chunk);

    void clean(Settlement settlement);

    boolean isClaim(Chunk chunk);

    String getClaim(Chunk chunk);

    Collection<String> getChunks(Settlement settlement);
}
