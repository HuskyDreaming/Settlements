package com.huskydreaming.settlements.services.implementations;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Singleton;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.ClaimService;
import com.huskydreaming.settlements.storage.Json;
import org.bukkit.Chunk;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class ClaimServiceImpl implements ClaimService {

    private Map<String, String> claims = new ConcurrentHashMap<>();

    @Override
    public void setClaim(String string, Chunk chunk) {
        claims.put(parse(chunk), string);
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
    public Collection<String> getChunks(Settlement settlement) {
        Multimap<String, String> multiMap = HashMultimap.create();
        for(Map.Entry<String, String> entry : claims.entrySet()) {
            multiMap.put(entry.getValue(), entry.getKey());
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
    }

    private String parse(Chunk chunk) {
        return chunk.getX() + ":" + chunk.getZ() + ":" + chunk.getWorld().getName();
    }
}
