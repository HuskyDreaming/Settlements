package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.services.base.ServiceInterface;
import org.bukkit.entity.Player;

public interface DependencyService extends ServiceInterface {
    boolean isTowny(Player player);

    boolean isWorldGuard(Player player);
}