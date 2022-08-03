package com.huskydreaming.settlements.services.implementations;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Singleton;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.ClaimService;
import com.huskydreaming.settlements.services.base.Service;
import com.huskydreaming.settlements.storage.Json;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Singleton
public class ClaimServiceImpl implements ClaimService {

    private Map<String, String> claims = new ConcurrentHashMap<>();

    @Override
    public void setClaim(Chunk chunk, Settlement settlement) {
        claims.put(parse(chunk), settlement.getName());
    }

    @Override
    public void removeClaim(Chunk chunk) {
        claims.remove(parse(chunk));
    }

    @Override
    public void clean(Settlement settlement) {
        getChunks(settlement).forEach(s -> claims.remove(s));
    }

    @Override
    public boolean isClaim(Chunk chunk) {
        return claims.containsKey(parse(chunk));
    }

    @Override
    public String getClaim(Chunk chunk) {
        return claims.get(parse(chunk));
    }

    @Override
    public Collection<String> getChunksAsStrings(Settlement settlement) {
        Multimap<String, String> multiMap = HashMultimap.create();
        for(Map.Entry<String, String> entry : claims.entrySet()) {
            multiMap.put(entry.getValue(), entry.getKey());
        }
        return multiMap.get(settlement.getName());
    }

    @Override
    public Collection<Chunk> getChunks(Settlement settlement) {
        Multimap<String, Chunk> multiMap = HashMultimap.create();
        for(Map.Entry<String, String> entry : claims.entrySet()) {
            multiMap.put(entry.getValue(), serialize(entry.getKey()));
        }
        return multiMap.get(settlement.getName());
    }


    @Override
    public void serialize(SettlementPlugin plugin) {
        Json.write(plugin, "claims", claims);
    }

    @Override
    public void deserialize(SettlementPlugin plugin) {
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        claims = Json.read(plugin, "claims", type);
        if(claims == null) claims = new ConcurrentHashMap<>();

        int size = claims.size();
        if(size > 0) {
            plugin.getLogger().info("Registered " + size + " claim(s).");
        }
    }

    private String parse(Chunk chunk) {
        return chunk.getX() + ":" + chunk.getZ() + ":" + chunk.getWorld().getName();
    }

    private Chunk serialize(String string) {
        String[] strings = string.split(":");
        World world = Bukkit.getWorld(strings[2]);
        if (world == null) return null;
        return world.getChunkAt(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
    }
}
