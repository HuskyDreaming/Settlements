package com.huskydreaming.settlements.services.implementations;

import com.google.gson.reflect.TypeToken;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.Json;
import com.huskydreaming.settlements.services.interfaces.TrustService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TrustServiceImpl implements TrustService {

    private Map<UUID, Set<String>> trusts;

    @Override
    public void deserialize(HuskyPlugin plugin) {
        Type type = new TypeToken<Map<UUID, Set<String>>>() {
        }.getType();
        trusts = Json.read(plugin, "data/trust", type);

        if (trusts == null) trusts = new ConcurrentHashMap<>();

        int trustSize = trusts.size();
        if (trustSize > 0) {
            plugin.getLogger().info("Registered " + trustSize + " trusted chunks(s).");
        }
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        Json.write(plugin, "data/trust", trusts);
    }

    @Override
    public void clean(String settlement) {
        List<UUID> uuids = trusts.entrySet().stream().filter(e -> e.getValue().contains(settlement))
                .map(Map.Entry::getKey)
                .toList();

        for (UUID uuid : uuids) {
            trusts.remove(uuid);
        }
    }

    @Override
    public void trust(OfflinePlayer offlinePlayer, String string) {
        Set<String> settlements = trusts.get(offlinePlayer.getUniqueId());
        if (settlements != null) {
            settlements.add(string);
            return;
        }

        settlements = new HashSet<>();
        settlements.add(string);

        trusts.put(offlinePlayer.getUniqueId(), settlements);
    }

    @Override
    public void unTrust(OfflinePlayer offlinePlayer, String string) {
        Set<String> settlements = trusts.get(offlinePlayer.getUniqueId());

        if (settlements == null) return;
        settlements.remove(string);

        if (!settlements.isEmpty()) return;
        trusts.remove(offlinePlayer.getUniqueId());
    }

    @Override
    public List<OfflinePlayer> getOfflinePlayers(String settlement) {
        return trusts.entrySet().stream()
                .filter(entry -> entry.getValue().contains(settlement))
                .map(entry -> Bukkit.getOfflinePlayer(entry.getKey()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasTrusts(OfflinePlayer offlinePlayer) {
        return trusts.containsKey(offlinePlayer.getUniqueId());
    }

    @Override
    public Set<String> getSettlements(OfflinePlayer offlinePlayer) {
        return Collections.unmodifiableSet(trusts.get(offlinePlayer.getUniqueId()));
    }
}