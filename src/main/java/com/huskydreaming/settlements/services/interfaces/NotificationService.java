package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.database.entities.Settlement;
import org.bukkit.entity.Player;

public interface NotificationService extends Service {

    void sendTrust(Player player, Settlement settlement);

    void sendWilderness(Player player);

    void sendSettlement(Player player, Settlement settlement, boolean isClaim);
}
