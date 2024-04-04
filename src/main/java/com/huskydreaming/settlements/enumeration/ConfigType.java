package com.huskydreaming.settlements.enumeration;

import com.huskydreaming.settlements.storage.types.Locale;

public enum ConfigType {
    DISABLED_WORLDS(Locale.CONFIG_DISABLED_WORLDS.parse()),
    PLACEHOLDER_STRING(Locale.CONFIG_PLACEHOLDER_STRING.parse()),
    ROLES(Locale.CONFIG_ROLES.parse()),
    FLAGS(Locale.CONFIG_FLAGS.parse()),
    TRUSTING(Locale.CONFIG_TRUSTING.parse()),
    TELEPORTATION(Locale.CONFIG_TELEPORTATION.parse()),
    NOTIFICATION(Locale.CONFIG_NOTIFICATION.parse()),
    SETTLEMENT(Locale.CONFIG_SETTLEMENT.parse());

    private final String description;

    ConfigType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", "-");
    }
}