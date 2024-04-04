package com.huskydreaming.settlements.enumeration;

import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.storage.types.Locale;

public enum Flag {
    ANIMAL_SPAWNING(Locale.FLAGS_ANIMAL_SPAWNING.parse()),
    MONSTER_SPAWNING(Locale.FLAGS_MONSTER_SPAWNING.parse()),
    ENTITY_GRIEF(Locale.FLAGS_ENTITY_GRIEF.parse()),
    LEAF_DECAY(Locale.FLAGS_LEAF_DECAY.parse()),
    EXPLOSIONS(Locale.FLAGS_EXPLOSIONS.parse());

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
