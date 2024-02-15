package com.huskydreaming.settlements.utilities;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
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
import java.util.*;

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

    public static void render(ClaimService claimService, Settlement settlement)
    {
        Collection<Chunk> chunks = claimService.getChunks(settlement);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.AQUA, 0.8f);
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
                if(!claimService.isClaim(north)) world.spawnParticle(Particle.REDSTONE, x, world.getHighestBlockYAt(x, minZ) + 1, minZ, 1, dustOptions);
                if(!claimService.isClaim(south)) world.spawnParticle(Particle.REDSTONE, x, world.getHighestBlockYAt(x, maxZ) + 1, maxZ, 1, dustOptions);
            }

            Chunk west = world.getChunkAt(chunkX - 1, chunkZ);
            Chunk east = world.getChunkAt(chunkX + 1, chunkZ);
            for (int z = minZ; z <= maxZ; z++) {
                if(!claimService.isClaim(west)) world.spawnParticle(Particle.REDSTONE, minX, world.getHighestBlockYAt(minX, z) + 1, z, 1, dustOptions);
                if(!claimService.isClaim(east)) world.spawnParticle(Particle.REDSTONE, maxX, world.getHighestBlockYAt(maxX, z) + 1, z, 1, dustOptions);
            }
        }
    }
}
