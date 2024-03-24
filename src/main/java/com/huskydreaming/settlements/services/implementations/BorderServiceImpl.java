package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.data.ChunkData;
import com.huskydreaming.settlements.services.interfaces.BorderService;
import com.huskydreaming.settlements.services.interfaces.ChunkService;
import com.huskydreaming.settlements.transience.BorderData;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BorderServiceImpl implements BorderService {

    private final ChunkService chunkService;
    private final Map<Player, BorderData> borders = new ConcurrentHashMap<>();

    public BorderServiceImpl(HuskyPlugin plugin) {
        chunkService = plugin.provide(ChunkService.class);
    }


    @Override
    public void deserialize(HuskyPlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<Player, BorderData> entry : borders.entrySet()) {
                    Player player = entry.getKey();
                    BorderData borderData = entry.getValue();
                    Particle.DustOptions dustOptions = new Particle.DustOptions(borderData.color(), 1.0f);

                    for (Location location : borderData.locations()) {
                        player.spawnParticle(Particle.REDSTONE, location, 1, dustOptions);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 6L);
    }

    @Override
    public void addPlayer(Player player, String name, Color color) {
        borders.put(player, calculatePositions(name, color));
    }

    @Override
    public void removePlayer(Player player) {
        borders.remove(player);
    }

    @Override
    public BorderData calculatePositions(String name, Color color) {
        Set<Location> points = new HashSet<>();
        for (ChunkData data : chunkService.getClaims(name)) {

            Chunk chunk = data.toChunk();
            World world = chunk.getWorld();

            int minX = data.getX() << 4;
            int minZ = data.getZ() << 4;

            int maxX = minX + 16;
            int maxZ = minZ + 16;

            // North
            if (!chunkService.isClaim(world.getChunkAt(chunk.getX(), chunk.getZ() - 1))) {
                for (int x = minX; x <= maxX; x++) {
                    points.add(new Location(world, x, world.getHighestBlockYAt(x, minZ) + 1, minZ));
                }
            }

            // South
            if (!chunkService.isClaim(world.getChunkAt(chunk.getX(), chunk.getZ() + 1))) {
                for (int x = minX; x <= maxX; x++) {
                    points.add(new Location(world, x, world.getHighestBlockYAt(x, maxZ) + 1, maxZ));
                }
            }

            // West
            if (!chunkService.isClaim(world.getChunkAt(chunk.getX() - 1, chunk.getZ()))) {
                for (int z = minZ; z <= maxZ; z++) {
                    points.add(new Location(world, minX, world.getHighestBlockYAt(minX, z) + 1, z));
                }
            }

            // East
            if (!chunkService.isClaim(world.getChunkAt(chunk.getX() + 1, chunk.getZ()))) {
                for (int z = minZ; z <= maxZ; z++) {
                    points.add(new Location(world, maxX, world.getHighestBlockYAt(maxX, z) + 1, z));
                }
            }
        }

        return new BorderData(color, points);
    }
}