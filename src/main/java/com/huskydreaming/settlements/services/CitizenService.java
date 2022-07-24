package com.huskydreaming.settlements.services;

import com.huskydreaming.settlements.persistence.Citizen;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import org.bukkit.OfflinePlayer;

import java.util.Collection;

public interface CitizenService extends ServiceInterface {

    boolean hasSettlement(OfflinePlayer offlinePlayer);

    void add(OfflinePlayer offlinePlayer, Settlement settlement);
    void remove(OfflinePlayer offlinePlayer);

    Citizen getCitizen(OfflinePlayer offlinePlayer);
    Collection<Citizen> getCitizens(Settlement settlement);
}
