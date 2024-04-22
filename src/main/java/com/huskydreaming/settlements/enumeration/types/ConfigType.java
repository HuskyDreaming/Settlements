package com.huskydreaming.settlements.enumeration.types;

import com.huskydreaming.settlements.storage.types.Message;

public enum ConfigType {
    DISABLED_WORLDS(Message.CONFIG_DISABLED_WORLDS.parse()),
    PLACEHOLDER_STRING(Message.CONFIG_PLACEHOLDER_STRING.parse()),
    ROLES(Message.CONFIG_ROLES.parse()),
    FLAGS(Message.CONFIG_FLAGS.parse()),
    TRUSTING(Message.CONFIG_TRUSTING.parse()),
    TELEPORTATION(Message.CONFIG_TELEPORTATION.parse()),
    NOTIFICATION(Message.CONFIG_NOTIFICATION.parse()),
    SETTLEMENT(Message.CONFIG_SETTLEMENT.parse());

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