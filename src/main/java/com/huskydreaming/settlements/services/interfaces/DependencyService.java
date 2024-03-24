package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import org.bukkit.entity.Player;

public interface DependencyService extends Service {
    boolean isTowny(Player player);

    boolean isWorldGuard(Player player);
}