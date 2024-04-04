package com.huskydreaming.settlements.services.implementations;

import com.google.common.reflect.TypeToken;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.data.ChunkData;
import com.huskydreaming.huskycore.storage.Json;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ClaimServiceImpl implements ClaimService {

    private Map<ChunkData, String> chunks;

    @Override
    public void serialize(HuskyPlugin plugin) {
        Json.write(plugin, "data/claims", chunks);
        plugin.getLogger().info("Saved " + chunks.size() + " claim(s).");
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        Type type = new TypeToken<Map<ChunkData, String>>() {}.getType();
        chunks = Json.read(plugin, "data/claims", type);
        if (chunks == null) chunks = new ConcurrentHashMap<>();

        int size = chunks.size();
        if (size > 0) plugin.getLogger().info("Registered " + size + " claim(s).");
    }

    @Override
    public void setClaim(Chunk chunk, String name) {
        chunks.put(ChunkData.deserialize(chunk), name);
    }

    @Override
    public void removeClaim(Chunk chunk) {
        chunks.remove(ChunkData.deserialize(chunk));
    }

    @Override
    public void clean(String name) {
        getClaims(name).forEach(claim -> chunks.remove(claim));
    }

    @Override
    public boolean isClaim(Chunk chunk) {
        return chunks.containsKey(ChunkData.deserialize(chunk));
    }

    @Override
    public String getClaim(Chunk chunk) {
        return chunks.get(ChunkData.deserialize(chunk));
    }

    @Override
    public int getCount() {
        return chunks.size();
    }

    @Override
    public LinkedHashMap<String, Long> getTop(int limit) {
        return chunks.values().stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public Set<ChunkData> getClaims(String name) {
        Set<ChunkData> claims = new HashSet<>();
        for (var entry : this.chunks.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(name)) {
                claims.add(entry.getKey());
            }
        }
        return claims;
    }

    @Override
    public boolean isAdjacent(String name, Chunk chunk) {
        boolean adjacent = false;
        for (ChunkData data : getClaims(name)) {
            if (Util.areAdjacentChunks(chunk, data.toChunk())) {
                adjacent = true;
            }
        }
        return adjacent;
    }

    @Override
    public boolean isAdjacentToOtherClaim(String string, Chunk chunk) {
        World world = chunk.getWorld();
        boolean adjacent = false;

        for (BlockFace blockFace : Util.chunkSteps) {
            int modX = blockFace.getModX();
            int modZ = blockFace.getModZ();
            Chunk adjacentChunk = world.getChunkAt(chunk.getX() + modX, chunk.getZ() + modZ);
            if (!isClaim(adjacentChunk)) continue;

            String claim = getClaim(adjacentChunk);
            if (!claim.equalsIgnoreCase(string)) adjacent = true;
        }
        return adjacent;
    }

    @Override
    public boolean isAdjacentToExistingClaim(Chunk chunk) {
        World world = chunk.getWorld();
        boolean adjacent = false;

        for (BlockFace blockFace : Util.chunkSteps) {
            int modX = blockFace.getModX();
            int modZ = blockFace.getModZ();
            Chunk adjacentChunk = world.getChunkAt(chunk.getX() + modX, chunk.getZ() + modZ);
            if (isClaim(adjacentChunk)) {
                adjacent = true;
            }
        }
        return adjacent;
    }
}