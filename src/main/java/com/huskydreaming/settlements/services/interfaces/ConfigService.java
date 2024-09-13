package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.database.persistence.Config;
import org.bukkit.entity.Player;

public interface ConfigService extends Service {

    boolean isDisabledWorld(Player player);

    void setup(HuskyPlugin plugin);

    Config getConfig();

    boolean setupLanguage(HuskyPlugin plugin);

    void setupConfig(HuskyPlugin plugin);
}