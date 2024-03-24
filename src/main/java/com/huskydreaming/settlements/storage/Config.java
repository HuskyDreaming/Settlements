package com.huskydreaming.settlements.storage;

public enum Config {
    DISABLED_WORLDS,
    PLACEHOLDER_STRING,
    ROLES,
    FLAGS,
    TELEPORTATION,
    SETTLEMENT;

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", "-");
    }
}