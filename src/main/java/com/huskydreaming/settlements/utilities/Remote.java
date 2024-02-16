package com.huskydreaming.settlements.utilities;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.storage.Extension;
import com.huskydreaming.settlements.transience.BorderData;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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

    public static String capitalizeFully(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        return Arrays.stream(input.split("\\s+"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
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
            if(string != null) string = string.replace("{" + i + "}", parameter);
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

    public static List<BorderData> calculatePositions(ClaimService claimService, Settlement settlement, Color color) {
        List<BorderData> data = new ArrayList<>();
        Collection<Chunk> chunks = claimService.getChunks(settlement);
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
                if(!claimService.isClaim(north)) data.add(new BorderData(x, world.getHighestBlockYAt(x, minZ) + 1, minZ, color));
                if(!claimService.isClaim(south)) data.add(new BorderData(x, world.getHighestBlockYAt(x, maxZ) + 1, maxZ, color));
            }

            Chunk west = world.getChunkAt(chunkX - 1, chunkZ);
            Chunk east = world.getChunkAt(chunkX + 1, chunkZ);
            for (int z = minZ; z <= maxZ; z++) {
                if(!claimService.isClaim(west)) data.add(new BorderData(minX, world.getHighestBlockYAt(minX, z) + 1, z, color));
                if(!claimService.isClaim(east)) data.add(new BorderData(maxX, world.getHighestBlockYAt(maxX, z) + 1, z, color));
            }
        }
        return data;
    }
}
