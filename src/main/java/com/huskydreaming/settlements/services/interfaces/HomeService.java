package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.database.dao.HomeDao;
import com.huskydreaming.settlements.database.entities.Home;
import com.huskydreaming.settlements.database.entities.Settlement;
import org.bukkit.entity.Player;

import java.util.Set;

public interface HomeService extends Service {

    void clean(Settlement settlement);

    void addHome(Home home);

    Home createHome(Player player, String name);

    void setHome(Settlement settlement, Player player, String name);

    void deleteHome(Settlement settlement, String name);

    boolean hasHome(Settlement settlement, String name);

    boolean hasHomes(Settlement settlement);

    Home getHome(Settlement settlement, String name);

    Set<Home> getHomes(Settlement settlement);

    HomeDao getDao();
}
