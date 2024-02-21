package com.huskydreaming.settlements.storage.types;

import com.huskydreaming.settlements.storage.base.Extension;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Yaml {

    private final String name;
    private Path path;
    private FileConfiguration configuration;

    public Yaml(String name) {
        this.name = name;
    }

    public void load(Plugin plugin) {
        path = Remote.create(plugin, name, Extension.YAML);
        configuration = YamlConfiguration.loadConfiguration(path.toFile());
    }

    public void save() {
        if(configuration == null || path == null || !Files.exists(path)) return;
        try {
            configuration.save(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reload(Plugin plugin) {
        configuration = YamlConfiguration.loadConfiguration(path.toFile());
        InputStream inputStream = plugin.getResource(getFileName());

        if(inputStream == null) return;

        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        YamlConfiguration defaultConfiguration = YamlConfiguration.loadConfiguration(reader);
        configuration.setDefaults(defaultConfiguration);

    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    private String getFileName() {
        return name + ".yml";
    }
}
