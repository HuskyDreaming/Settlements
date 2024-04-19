package com.huskydreaming.settlements.services.implementations;

import com.google.common.reflect.TypeToken;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.Json;
import com.huskydreaming.settlements.services.interfaces.HomeService;
import com.huskydreaming.settlements.storage.persistence.Home;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HomeServiceImpl implements HomeService {

    private Map<String, List<Home>> homes;

    @Override
    public void deserialize(HuskyPlugin plugin) {
        Type type = new TypeToken<Map<String, List<Home>>>() {
        }.getType();
        homes = Json.read(plugin, "data/homes", type);
        if (homes == null) homes = new ConcurrentHashMap<>();
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        Json.write(plugin, "data/homes", homes);
    }

    @Override
    public void setHome(String settlement, String name, Player player) {
        homes.computeIfAbsent(settlement, s -> new ArrayList<>())
                .add(Home.create(name, player.getLocation()));
    }

    @Override
    public void deleteHome(String settlement, String name) {
        homes.get(settlement).removeIf(home -> home.name().equalsIgnoreCase(name));
    }

    @Override
    public boolean hasHome(String settlement, String name) {
        return homes.containsKey(settlement) && homes.get(settlement).stream()
                .anyMatch(home -> home.name().equalsIgnoreCase(name));
    }

    @Override
    public boolean hasHomes(String settlement) {
        return homes.containsKey(settlement);
    }

    @Override
    public Home getHome(String settlement, String name) {
        return homes.get(settlement).stream()
                .filter(home -> home.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }


    @Override
    public List<Home> getHomes(String settlement) {
        if (!homes.containsKey(settlement)) return new ArrayList<>();
        return Collections.unmodifiableList(homes.get(settlement));
    }
}