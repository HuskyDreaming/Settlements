package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface DependencyService extends Service {

    boolean isTowny(Player player);

    boolean isTowny(Player player, Block block);

    boolean isWorldGuard(Player player);

    boolean isWorldGuard(Block block);
}