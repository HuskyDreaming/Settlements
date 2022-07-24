package com.huskydreaming.settlements.utilities;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionPoint;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class Remote {

    public static String parameterize(Locale locale, String... strings) {
        String string = locale.parse();
        for(int i = 0; i < strings.length; i++) {
            string = string.replace("{" + i + "}", strings[i]);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static OfflinePlayer getOfflinePlayer(String name) {
        for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if(offlinePlayer.getName() != null && offlinePlayer.getName().equalsIgnoreCase(name)) {
                return offlinePlayer;
            }
        }
        return null;
    }

    public static String prefix(Locale locale, String... strings) {
        String string = locale.parse();
        for(int i = 0; i < strings.length; i++) {
            string = string.replace("{" + i + "}", strings[i]);
        }
        return ChatColor.translateAlternateColorCodes('&', Locale.PREFIX.parse() + string);
    }

    public static boolean isInjected(Injector injector, Object object) {
        Map<TypeLiteral<?>, List<InjectionPoint>> injectionPoints = injector.getAllMembersInjectorInjectionPoints();
        return injectionPoints.containsKey(TypeLiteral.get(object.getClass()));
    }

    public static boolean areAdjacentChunks(Chunk a, Chunk b) {
        World world = a.getWorld();
        if (!world.equals(b.getWorld())) {
            return false;
        }

        BlockFace[] steps = new BlockFace[]{
                BlockFace.NORTH,
                BlockFace.EAST,
                BlockFace.SOUTH,
                BlockFace.WEST
        };

        for (BlockFace step : steps) {
            if (world.getChunkAt(a.getX() + step.getModX(), a.getZ() + step.getModZ()).equals(b)) {
                return true;
            }
        }

        return false;
    }

    public static void render(Player player, Chunk chunk, Color color)
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
