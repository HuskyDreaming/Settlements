package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.BorderService;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.transience.BorderData;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BorderServiceImpl implements BorderService {

    private final ClaimService claimService;
    private final SettlementService settlementService;
    private final Map<Player, BorderData> borders = new ConcurrentHashMap<>();

    public BorderServiceImpl() {
        claimService = ServiceProvider.Provide(ClaimService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void addPlayer(Player player, String settlementName, Color color) {
        Settlement settlement = settlementService.getSettlement(settlementName);
        if(settlement != null) borders.put(player, calculatePositions(settlement, color));
    }

    @Override
    public void removePlayer(Player player) {
        borders.remove(player);
    }

    @Override
    public void run(Plugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Map.Entry<Player, BorderData> entry : borders.entrySet()) {
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
    public BorderData calculatePositions(Settlement settlement, Color color) {
        Collection<Chunk> chunks = claimService.getChunks(settlement);
        Set<Location> points = new HashSet<>();
        for (Chunk chunk : chunks) {

            World world = chunk.getWorld();
            int chunkX = chunk.getX();
            int chunkZ = chunk.getZ();

            int minX = chunkX << 4;
            int minZ = chunkZ << 4;
            int maxX = minX + 15;
            int maxZ = minZ + 15;

            Chunk north = world.getChunkAt(chunkX, chunkZ - 1);
            Chunk south = world.getChunkAt(chunkX, chunkZ + 1);
            for (int x = minX; x <= maxX; x++) {
                if(!claimService.isClaim(north)) points.add(new Location(world, x, world.getHighestBlockYAt(x, minZ) + 1, minZ));
                if(!claimService.isClaim(south)) points.add(new Location(world,x, world.getHighestBlockYAt(x, maxZ) + 1, maxZ));
            }

            Chunk west = world.getChunkAt(chunkX - 1, chunkZ);
            Chunk east = world.getChunkAt(chunkX + 1, chunkZ);
            for (int z = minZ; z <= maxZ; z++) {
                if(!claimService.isClaim(west)) points.add(new Location(world,minX, world.getHighestBlockYAt(minX, z) + 1, z));
                if(!claimService.isClaim(east)) points.add(new Location(world,maxX, world.getHighestBlockYAt(maxX, z) + 1, z));
            }
        }

        return new BorderData(color, points);
    }
}
