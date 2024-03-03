package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.services.base.ServiceInterface;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Collection;
import java.util.LinkedHashMap;

public interface ClaimService extends ServiceInterface {

    void setClaim(Chunk chunk, String name);

    void removeClaim(Chunk chunk);

    void clean(String name);

    boolean isClaim(Chunk chunk);

    String getClaim(Chunk chunk);

    int getCount();

    LinkedHashMap<String, Long> getTop(int limit);

    Collection<String> getChunksAsStrings(String name);

    Collection<Chunk> getChunks(String name);

    boolean isDisabledWorld(World world);
}