package com.huskydreaming.settlements.services.implementations;

import com.google.gson.reflect.TypeToken;
import com.google.inject.Singleton;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Citizen;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.CitizenService;
import com.huskydreaming.settlements.services.base.Service;
import com.huskydreaming.settlements.storage.Json;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Singleton
public class CitizenServiceImpl implements CitizenService {

    private Map<UUID, Citizen> citizens = new ConcurrentHashMap<>();

    @Override
    public boolean hasSettlement(OfflinePlayer offlinePlayer) {
        return citizens.containsKey(offlinePlayer.getUniqueId());
    }

    @Override
    public void add(OfflinePlayer offlinePlayer, Settlement settlement) {
        citizens.put(offlinePlayer.getUniqueId(), Citizen.create(settlement, settlement.getDefaultRole()));
    }

    @Override
    public void remove(OfflinePlayer offlinePlayer) {
        citizens.remove(offlinePlayer.getUniqueId());
    }

    @Override
    public void clean(Settlement settlement) {
        citizens.values().removeIf(citizen -> citizen.getSettlement().equalsIgnoreCase(settlement.getName()));
    }

    @Override
    public Citizen getCitizen(OfflinePlayer offlinePlayer) {
        return citizens.get(offlinePlayer.getUniqueId());
    }

    @Override
    public Collection<Citizen> getCitizens(Settlement settlement) {
        return null;
    }


    @Override
    public void serialize(SettlementPlugin plugin) {
        Json.write(plugin, "citizens", citizens);
    }

    @Override
    public void deserialize(SettlementPlugin plugin) {
        Type type = new TypeToken<Map<UUID, Citizen>>(){}.getType();
        citizens = Json.read(plugin, "citizens", type);
        if(citizens == null) citizens = new ConcurrentHashMap<>();

        int size = citizens.size();
        if(size > 0) {
            plugin.getLogger().info("Registered " + size + " citizen(s).");
        }
    }
}
