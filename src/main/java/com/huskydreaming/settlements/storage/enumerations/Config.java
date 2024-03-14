package com.huskydreaming.settlements.storage.enumerations;

public enum Config {
    DISABLED_WORLDS,
    PLACEHOLDER_STRING,
    ROLES,
    SETTLEMENT;

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", "-");
    }
}