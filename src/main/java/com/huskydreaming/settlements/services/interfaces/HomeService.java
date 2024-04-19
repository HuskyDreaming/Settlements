package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.storage.persistence.Home;
import org.bukkit.entity.Player;

import java.util.List;

public interface HomeService extends Service {

    void setHome(String settlement, String name, Player player);

    void deleteHome(String settlement, String name);

    boolean hasHome(String settlement, String name);

    boolean hasHomes(String settlement);

    Home getHome(String settlement, String name);

    List<Home> getHomes(String settlement);
}
