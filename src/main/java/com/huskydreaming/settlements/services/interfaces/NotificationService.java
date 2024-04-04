package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import org.bukkit.entity.Player;

public interface NotificationService extends Service {

    void sendWilderness(Player player);

    void sendSettlement(Player player, String chunk, String description, boolean isClaim);
}
