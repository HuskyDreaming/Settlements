package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.enumeration.types.NotificationType;
import org.bukkit.entity.Player;

public interface NotificationService extends Service {

    void sendTrust(Player player, String name, String description);

    void sendWilderness(Player player);

    void sendSettlement(Player player, String chunk, String description, boolean isClaim);
}
