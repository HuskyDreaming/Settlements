package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.Set;

public interface TrustService extends Service {

    void clean(String settlement);

    void trust(OfflinePlayer offlinePlayer, String string);

    void unTrust(OfflinePlayer offlinePlayer, String string);

    List<OfflinePlayer> getOfflinePlayers(String settlement);

    boolean hasTrusts(OfflinePlayer offlinePlayer);

    Set<String> getSettlements(OfflinePlayer offlinePlayer);
}
