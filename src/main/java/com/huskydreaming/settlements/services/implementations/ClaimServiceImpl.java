package com.huskydreaming.settlements.services.implementations;

import com.google.gson.reflect.TypeToken;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Claim;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.storage.types.Json;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ClaimServiceImpl implements ClaimService {

    private final ConfigService configService;
    private Map<Claim, String> claims = new ConcurrentHashMap<>();
    private List<String> disabledWorlds;


    public ClaimServiceImpl(SettlementPlugin settlementPlugin) {
        configService = settlementPlugin.provide(ConfigService.class);
    }

    @Override
    public void setClaim(Chunk chunk, String name) {
        claims.put(Claim.deserialize(chunk), name);
    }

    @Override
    public void removeClaim(Chunk chunk) {
        claims.remove(Claim.deserialize(chunk));
    }

    @Override
    public void clean(String name) {
        getClaims(name).forEach(claim -> claims.remove(claim));
    }

    @Override
    public boolean isClaim(Chunk chunk) {
        return claims.containsKey(Claim.deserialize(chunk));
    }

    @Override
    public String getClaim(Chunk chunk) {
        return claims.get(Claim.deserialize(chunk));
    }

    @Override
    public int getCount() {
        return claims.size();
    }

    @Override
    public LinkedHashMap<String, Long> getTop(int limit) {
        return claims.values().stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public Set<Claim> getClaims(String name) {
        Set<Claim> claims = new HashSet<>();
        for (var entry : this.claims.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(name)) {
                claims.add(entry.getKey());
            }
        }
        return claims;
    }

    @Override
    public boolean isDisabledWorld(World world) {
        return disabledWorlds.contains(world.getName());
    }


    @Override
    public void serialize(SettlementPlugin plugin) {
        Json.write(plugin, "data/claims", claims);
        plugin.getLogger().info("Saved " + claims.size() + " claim(s).");
    }

    @Override
    public void deserialize(SettlementPlugin plugin) {
        Type type = new TypeToken<Map<Claim, String>>() {}.getType();
        claims = Json.read(plugin, "data/claims", type);
        if (claims == null) claims = new ConcurrentHashMap<>();

        int size = claims.size();
        if (size > 0) plugin.getLogger().info("Registered " + size + " claim(s).");

        if (disabledWorlds != null) disabledWorlds.clear();
        disabledWorlds = configService.deserializeDisabledWorlds(plugin);
    }
}