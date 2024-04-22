package com.huskydreaming.settlements.enumeration;

import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.storage.types.Message;

public enum Flag {
    ANIMAL_KILLING(Message.FLAGS_ANIMAL_KILLING.parse()),
    ANIMAL_SPAWNING(Message.FLAGS_ANIMAL_SPAWNING.parse()),
    MONSTER_SPAWNING(Message.FLAGS_MONSTER_SPAWNING.parse()),
    ENTITY_GRIEF(Message.FLAGS_ENTITY_GRIEF.parse()),
    LEAF_DECAY(Message.FLAGS_LEAF_DECAY.parse()),
    PVP(Message.FLAGS_LEAF_DECAY.parse()),
    EXPLOSIONS(Message.FLAGS_EXPLOSIONS.parse()),
    END_PORTAL(Message.FLAGS_EXPLOSIONS.parse()),
    LAVA_SPREAD(Message.FLAGS_EXPLOSIONS.parse()),
    ENDER_PEARL(Message.FLAGS_EXPLOSIONS.parse());

    private final String description;

    Flag(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return Util.capitalize(name().toLowerCase().replace("_", " "));
    }
}
