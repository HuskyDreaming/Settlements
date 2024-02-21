package com.huskydreaming.settlements.persistence;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class Settlement {

    private UUID owner;
    private String name;
    private String tag;
    private String description;
    private String defaultRole;
    private Location location;
    private Material icon;

    private int maxLand;
    private int maxCitizens;
    private int maxRoles;

    public static Settlement create(Player player, String name) {
        return new Settlement(player, name);
    }

    public Settlement(Player player, String name) {
        this.owner = player.getUniqueId();
        this.name = name;
        this.maxCitizens = 10;
        this.maxLand = 15;
        this.maxRoles = 5;
        this.description = "A peaceful place.";
        this.location = player.getLocation();
    }

    public void setOwner(OfflinePlayer offlinePlayer) {
        this.owner = offlinePlayer.getUniqueId();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setMaxCitizens(int maxCitizens) {
        this.maxCitizens = maxCitizens;
    }

    public void setMaxLand(int maxLand) {
        this.maxLand = maxLand;
    }

    public void setMaxRoles(int maxRoles) {
        this.maxRoles = maxRoles;
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public boolean isOwner(OfflinePlayer offlinePlayer) {
        return owner.equals(offlinePlayer.getUniqueId());
    }


    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOwnerName() {
        OfflinePlayer player = Arrays.stream(Bukkit.getOfflinePlayers())
                .filter(offlinePlayer -> offlinePlayer.getUniqueId().equals(owner))
                .findFirst().orElse(null);
        if(player != null) return player.getName();
        return null;
    }

    public int getMaxLand() {
        return maxLand;
    }

    public int getMaxCitizens() {
        return maxCitizens;
    }

    public String getDefaultRole() {
        return defaultRole;
    }

    public int getMaxRoles() {
        return maxRoles;
    }

    public Material getIcon() {
        return icon;
    }

    public String getTag() {
        return tag;
    }
}
