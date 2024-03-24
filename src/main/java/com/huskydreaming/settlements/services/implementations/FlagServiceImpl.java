package com.huskydreaming.settlements.services.implementations;

import com.google.common.reflect.TypeToken;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.Json;
import com.huskydreaming.settlements.persistence.Flag;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.FlagService;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FlagServiceImpl implements FlagService {

    private Map<String, Set<Flag>> flags;
    private final Set<Flag> defaultFlags;

    public FlagServiceImpl(HuskyPlugin plugin) {
        ConfigService configService = plugin.provide(ConfigService.class);
        defaultFlags = configService.deserializeDefaultFlags(plugin);
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        Json.write(plugin, "data/flags", flags);
        plugin.getLogger().info("Saved " + flags.size() + " flags(s).");
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        Type type = new TypeToken<Map<String, Set<Flag>>>() {
        }.getType();
        flags = Json.read(plugin, "data/flags", type);
        if (flags == null) flags = new ConcurrentHashMap<>();

        int size = flags.size();
        if (size > 0) plugin.getLogger().info("Registered " + size + " settlement flags(s).");
    }

    @Override
    public void clean(String string) {
        flags.remove(string);
    }

    @Override
    public void setup(String string) {
        flags.put(string, defaultFlags != null ? defaultFlags : new HashSet<>());
    }

    @Override
    public void addFlag(String string, Flag flag) {
        if (flags.containsKey(string)) {
            flags.get(string).add(flag);
        } else {
            Set<Flag> flags = new HashSet<>();
            flags.add(flag);
            this.flags.put(string, flags);
        }
    }

    @Override
    public void removeFlag(String string, Flag flag) {
        if (flags.containsKey(string)) {
            flags.get(string).remove(flag);
        } else {
            Set<Flag> flags = new HashSet<>();
            this.flags.put(string, flags);
        }
    }

    @Override
    public boolean hasFlag(String string, Flag flag) {
        if (flags.containsKey(string)) {
            return flags.get(string).contains(flag);
        } else {
            Set<Flag> flags = new HashSet<>();
            this.flags.put(string, flags);
            return false;
        }
    }
}