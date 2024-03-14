package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Claim;
import com.huskydreaming.settlements.services.interfaces.BorderService;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.transience.BorderData;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BorderServiceImpl implements BorderService {

    private final ClaimService claimService;
    private final Map<Player, BorderData> borders = new ConcurrentHashMap<>();

    public BorderServiceImpl(SettlementPlugin plugin) {
        claimService = plugin.provide(ClaimService.class);
    }

    @Override
    public void deserialize(SettlementPlugin plugin) {
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
        for (Claim claim : claimService.getClaims(name)) {

            Chunk chunk = claim.toChunk();
            World world = chunk.getWorld();

            int minX = claim.getX() << 4;
            int minZ = claim.getZ() << 4;

            int maxX = minX + 16;
            int maxZ = minZ + 16;

            // North
            if (!claimService.isClaim(world.getChunkAt(chunk.getX(), chunk.getZ() - 1))) {
                for (int x = minX; x <= maxX; x++) {
                    points.add(new Location(world, x, world.getHighestBlockYAt(x, minZ) + 1, minZ));
                }
            }

            // South
            if (!claimService.isClaim(world.getChunkAt(chunk.getX(), chunk.getZ() + 1))) {
                for (int x = minX; x <= maxX; x++) {
                    points.add(new Location(world, x, world.getHighestBlockYAt(x, maxZ) + 1, maxZ));
                }
            }

            // West
            if (!claimService.isClaim(world.getChunkAt(chunk.getX() - 1, chunk.getZ()))) {
                for (int z = minZ; z <= maxZ; z++) {
                    points.add(new Location(world, minX, world.getHighestBlockYAt(minX, z) + 1, z));
                }
            }

            // East
            if (!claimService.isClaim(world.getChunkAt(chunk.getX() + 1, chunk.getZ()))) {
                for (int z = minZ; z <= maxZ; z++) {
                    points.add(new Location(world, maxX, world.getHighestBlockYAt(maxX, z) + 1, z));
                }
            }
        }

        return new BorderData(color, points);
    }
}