package com.huskydreaming.settlements.storage.persistence;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Objects;

public record Home(String name, Material material, Location location) {

    public static Home create(String name, Location location) {
        return new Home(name, Material.WHITE_BED, location);
    }

    public static Home create(String name, Material material, Location location) {
        return new Home(name, material, location);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Home home)) return false;
        return Objects.equals(name, home.name) && Objects.equals(location, home.location);
    }
}