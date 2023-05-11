package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import org.bukkit.Chunk;

import java.util.Collection;


public interface ClaimService extends ServiceInterface {

    void setClaim(Chunk chunk, Settlement settlement);

    void removeClaim(Chunk chunk);

    void clean(Settlement settlement);

    boolean isClaim(Chunk chunk);

    String getClaim(Chunk chunk);

    Collection<String> getChunksAsStrings(Settlement settlement);

    Collection<Chunk> getChunks(Settlement settlement);
}
