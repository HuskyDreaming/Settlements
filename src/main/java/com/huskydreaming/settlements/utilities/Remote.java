package com.huskydreaming.settlements.utilities;

import com.huskydreaming.settlements.storage.base.Extension;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Remote {

    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isNumeric(String string) {
        return pattern.matcher(string).matches();
    }

    public static Path create(Plugin plugin, String fileName, Extension extension) {
        Path path = Paths.get(plugin.getDataFolder() + File.separator + fileName + extension.toString());
        Path parentPath = path.getParent();
        try {
            if (!Files.exists(parentPath)) {
                Files.createDirectories(parentPath);
                plugin.getLogger().info("Created new directory: " + parentPath.getFileName());
            }
            if (!Files.exists(path)) {
                Files.createFile(path);
                plugin.getLogger().info("Created new file: " + path.getFileName());
            }
        } catch (IOException e) {
            plugin.getLogger().severe(e.getMessage());
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

    public static OfflinePlayer getOfflinePlayer(String name) {
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.getName() != null && offlinePlayer.getName().equalsIgnoreCase(name)) {
                return offlinePlayer;
            }
        }
        return null;
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
}