package com.huskydreaming.settlements.utilities;

import org.bukkit.*;
import org.bukkit.entity.Player;

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
