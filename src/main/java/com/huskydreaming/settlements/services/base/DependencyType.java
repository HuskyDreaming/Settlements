package com.huskydreaming.settlements.services.base;

public enum DependencyType {
    WORLDGUARD("WorldGuard"),
    PLACEHOLDER_API("PlaceholderAPI");

    private final String string;

    DependencyType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
