package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.implementations.RepositoryImpl;
import com.huskydreaming.huskycore.interfaces.Repository;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.SqlType;
import com.huskydreaming.settlements.database.dao.HomeDao;
import com.huskydreaming.settlements.database.entities.Home;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.services.interfaces.HomeService;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HomeServiceImpl implements HomeService {

    private final HomeDao homeDao;
    private final Repository<Home> homeRepository;

    public HomeServiceImpl(SettlementPlugin plugin) {
        homeDao = new HomeDao(plugin);
        homeRepository = new RepositoryImpl<>();
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        homeDao.bulkImport(SqlType.HOME, homeRepository::bulkAdd);
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        homeDao.bulkUpdate(SqlType.HOME, homeRepository.values());
    }

    @Override
    public void clean(Settlement settlement) {
        Set<Long> homeIds = homeRepository.entries().stream()
                .filter(h -> h.getValue().getSettlementId() == settlement.getId())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        homeDao.bulkDelete(SqlType.HOME, homeIds);
        homeRepository.keys().removeAll(homeIds);
    }

    @Override
    public void addHome(Home home) {
        homeRepository.add(home);
    }

    @Override
    public Home createHome(Player player, String name) {
        Location location = player.getLocation();
        World world = location.getWorld();
        if(world == null) return null;

        Home home = new Home();
        home.setName(name);
        home.setWorldUID(world.getUID());
        home.setX(location.getX());
        home.setY(location.getY());
        home.setZ(location.getZ());
        home.setYaw(location.getYaw());
        home.setPitch(location.getPitch());
        return home;
    }

    @Override
    public void setHome(Settlement settlement, Player player, String name) {
        Home home = createHome(player, name);
        home.setSettlementId(settlement.getId());
        homeDao.insert(home).queue(i -> {
            home.setId(i);
            homeRepository.add(home);
        });
    }

    @Override
    public void deleteHome(Settlement settlement, String name) {
        homeRepository.values().stream()
                .filter(v -> v.getSettlementId() == settlement.getId() && v.getName().equalsIgnoreCase(name))
                .findFirst().ifPresent(home -> homeDao.delete(home).queue(success -> {
                    if (success) homeRepository.remove(home);
                }));
    }

    @Override
    public boolean hasHome(Settlement settlement, String name) {
        return homeRepository.values().stream()
                .anyMatch(h -> h.getSettlementId() == settlement.getId() && h.getName().equalsIgnoreCase(name));
    }

    @Override
    public boolean hasHomes(Settlement settlement) {
        return homeRepository.values().stream()
                .anyMatch(h -> h.getSettlementId() == settlement.getId());
    }

    @Override
    public Home getHome(Settlement settlement, String name) {
        return homeRepository.values().stream()
                .filter(h -> h.getSettlementId() == settlement.getId() && h.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Set<Home> getHomes(Settlement settlement) {
        return homeRepository.values().stream().filter(h -> h.getSettlementId() == settlement.getId()).collect(Collectors.toSet());
    }

    @Override
    public HomeDao getDao() {
        return homeDao;
    }
}