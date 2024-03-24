package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.persistence.Flag;

public interface FlagService extends Service {
    void clean(String name);

    void setup(String string);

    void addFlag(String string, Flag flag);

    void removeFlag(String string, Flag flag);

    boolean hasFlag(String string, Flag flag);
}