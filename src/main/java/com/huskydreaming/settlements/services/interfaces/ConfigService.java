package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.enumeration.types.NotificationType;
import org.bukkit.entity.Player;

public interface ConfigService extends Service {

    boolean isDisabledWorld(Player player);

    Config getConfig();

    Config setupLanguage(HuskyPlugin plugin);

    void setupConfig(HuskyPlugin plugin, Config config);
}