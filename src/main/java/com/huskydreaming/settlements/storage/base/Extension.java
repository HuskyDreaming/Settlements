package com.huskydreaming.settlements.storage.base;

public enum Extension {
    JSON,
    YAML;

    @Override
    public String toString() {
        return "." + name().toLowerCase();
    }
}