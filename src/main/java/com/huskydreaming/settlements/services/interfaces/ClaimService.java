package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Collection;
import java.util.LinkedHashMap;

public interface ClaimService extends ServiceInterface {

    void setClaim(Chunk chunk, Settlement settlement);

    void removeClaim(Chunk chunk);

    void clean(Settlement settlement);

    boolean isClaim(Chunk chunk);

    String getClaim(Chunk chunk);

    int getCount();

    LinkedHashMap<String, Long> getTop(int limit);

    Collection<String> getChunksAsStrings(Settlement settlement);

    Collection<Chunk> getChunks(Settlement settlement);

    boolean isDisabledWorld(World world);
}
