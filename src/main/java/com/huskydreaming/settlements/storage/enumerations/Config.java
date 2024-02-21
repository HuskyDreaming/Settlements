package com.huskydreaming.settlements.storage.enumerations;

public enum Config {
    DISABLED_WORLDS("disabled-worlds"),
    PLACEHOLDER_STRING("placeholder-string"),
    ROLES("roles"),
    SETTLEMENT("settlement");

    private final String path;

    Config(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
