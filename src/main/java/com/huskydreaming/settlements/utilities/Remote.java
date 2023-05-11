package com.huskydreaming.settlements.utilities;

import com.huskydreaming.settlements.storage.Extension;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Remote {

    public static Path create(Plugin plugin, String fileName, Extension extension) {
        Path path = Paths.get(plugin.getDataFolder() + File.separator + fileName + extension.toString());
        Path parentPath = path.getParent();
        try {
            if(!Files.exists(parentPath)) {
                Files.createDirectories(parentPath);
                plugin.getLogger().info("Created new directory: " + parentPath.getFileName());
            }
            if(!Files.exists(path)) {
                Files.createFile(path);
                plugin.getLogger().info("Created new file: " + path.getFileName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String parameterize(Parseable parseable, Object... objects) {
        String string = parseable.parse();
        for(int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            String parameter = (object instanceof String stringObject) ? stringObject : String.valueOf(object);
            string = string.replace("{" + i + "}", parameter);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> parameterizeList(Parseable parseable, Object... objects) {
        List<String> stringList = parseable.parseList();
        List<String> parameterList = new ArrayList<>();
        for(String string : stringList) {
            for(int i = 0; i < objects.length; i++) {
                string = string.replace("{" + i + "}", String.valueOf(objects[i]));
            }
            parameterList.add(string);
        }
        return parameterList;
    }

    public static OfflinePlayer getOfflinePlayer(String name) {
        for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if(offlinePlayer.getName() != null && offlinePlayer.getName().equalsIgnoreCase(name)) {
                return offlinePlayer;
            }
        }
        return null;
    }

    public static String prefix(Locale locale, Object... objects) {
        String string = locale.parse();
        for(int i = 0; i < objects.length; i++) {
            String parameter = (objects[i] instanceof String stringObject) ? stringObject : String.valueOf(objects[i]);
            string = string.replace("{" + i + "}", parameter);
        }
        return ChatColor.translateAlternateColorCodes('&', Locale.PREFIX.parse() + string);
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
