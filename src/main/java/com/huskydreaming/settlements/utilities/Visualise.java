package com.huskydreaming.settlements.utilities;

import com.huskydreaming.settlements.managers.SettlementManager;
import com.huskydreaming.settlements.persistence.Settlement;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.HeightMap;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Visualise {

    public static void render(Player player, SettlementManager settlementManager) {
        Color color;
        Chunk chunk = player.getLocation().getChunk();
        Settlement settlement = settlementManager.getSettlement(chunk);
        if(settlement != null) {
            if(settlement.isCitizen(player)) {
                color = Color.AQUA;
            } else {
                color = Color.FUCHSIA;
            }
        } else {
            color = Color.LIME;
        }
        render(player, chunk, color);
    }

    private static void render(Player player, Chunk chunk, Color color)
    {
        int minX = chunk.getX() * 16;
        int minZ = chunk.getZ() * 16;
        int y;

        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 0.8f);

        for(int x = minX; x < minX + 16; x++) {
            for(int z = minZ; z < minZ + 16; z++) {
                y = chunk.getWorld().getHighestBlockYAt(minX, z, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                player.spawnParticle(Particle.REDSTONE, minX, y + 1, z, 1, dustOptions);

                y = chunk.getWorld().getHighestBlockYAt(x, minZ, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                player.spawnParticle(Particle.REDSTONE, x , y + 1, minZ, 1, dustOptions);

                y = chunk.getWorld().getHighestBlockYAt(minX + 16, z, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                player.spawnParticle(Particle.REDSTONE, minX + 16, y + 1, z, 1, dustOptions);

                y = chunk.getWorld().getHighestBlockYAt(x, minZ + 16, HeightMap.MOTION_BLOCKING_NO_LEAVES);
                player.spawnParticle(Particle.REDSTONE, x , y + 1, minZ + 16, 1, dustOptions);
            }
        }
    }
}
