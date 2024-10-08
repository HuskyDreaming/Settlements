package com.huskydreaming.settlements.enumeration.types;

public enum DependencyType {
    WORLD_GUARD("WorldGuard"),
    PLACEHOLDER_API("PlaceholderAPI"),
    TOWNY("Towny");

    private final String string;

    DependencyType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}