package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.data.ChunkData;
import com.huskydreaming.huskycore.interfaces.Service;
import org.bukkit.Chunk;

import java.util.LinkedHashMap;
import java.util.Set;

public interface ClaimService extends Service {

    void setClaim(Chunk chunk, String name);

    void removeClaim(Chunk chunk);

    void clean(String name);

    boolean isClaim(Chunk chunk);

    String getClaim(Chunk chunk);

    int getCount();

    LinkedHashMap<String, Long> getTop(int limit);

    Set<ChunkData> getClaims(String name);

    boolean isAdjacent(String name, Chunk chunk);

    boolean isAdjacentToOtherClaim(String string, Chunk chunk);

    boolean isAdjacentToExistingClaim(Chunk chunk);
}