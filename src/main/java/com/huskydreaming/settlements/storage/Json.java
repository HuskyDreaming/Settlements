package com.huskydreaming.settlements.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.huskydreaming.settlements.serializers.LocationSerializer;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Json {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Location.class, new LocationSerializer())
            .setPrettyPrinting()
            .create();

    public static void write(Plugin plugin, String fileName, Object object) {
        Path path = Remote.create(plugin, fileName, Extension.JSON);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            GSON.toJson(object, bufferedWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T read(Plugin plugin, String fileName, Type type) {
        Path path = Remote.create(plugin, fileName, Extension.JSON);
        try {
            BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            JsonReader jsonReader = new JsonReader(bufferedReader);
            return GSON.fromJson(jsonReader, type);
        } catch (IOException e) {
            return null;
        }
    }
}
