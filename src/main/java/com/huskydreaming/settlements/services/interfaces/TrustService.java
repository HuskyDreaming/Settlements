package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.database.entities.Settlement;
import org.bukkit.OfflinePlayer;

import java.util.Set;

public interface TrustService extends Service {

    void clean(Settlement settlement);
    void trust(OfflinePlayer offlinePlayer, Settlement settlement);

    void unTrust(OfflinePlayer offlinePlayer, Settlement settlement);

    void unTrust(OfflinePlayer offlinePlayer, long settlementId);

    Set<OfflinePlayer> getOfflinePlayers(Settlement settlement);

    Set<OfflinePlayer> getOfflinePlayers(long settlementId);

    boolean isTrusted(OfflinePlayer offlinePlayer, Settlement settlement);

    boolean hasTrusts(OfflinePlayer offlinePlayer);

    Set<Long> getSettlementIds(OfflinePlayer offlinePlayer);
}
