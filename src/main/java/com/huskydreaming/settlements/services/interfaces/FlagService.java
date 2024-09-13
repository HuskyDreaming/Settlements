package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.database.entities.Claim;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.enumeration.FlagType;

public interface FlagService extends Service {

    void clean(Settlement settlement);

    void addFlag(Settlement settlement, FlagType flagType, Runnable runnable);

    void removeFlag(Settlement settlement, FlagType flagType, Runnable runnable);

    boolean hasFlag(Settlement settlement, FlagType flagType);

    boolean hasFlag(Claim claim, FlagType flagType);
}