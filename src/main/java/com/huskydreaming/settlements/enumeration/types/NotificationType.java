package com.huskydreaming.settlements.enumeration.types;

import com.huskydreaming.huskycore.utilities.Util;

public enum NotificationType {
    NONE,
    BOSS_BAR,
    ACTION_BAR,
    TITLE;

    @Override
    public String toString() {
        return Util.capitalize(name().toLowerCase().replace("_", " "));
    }
}
