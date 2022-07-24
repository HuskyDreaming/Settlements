package com.huskydreaming.settlements.persistence;

import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RoleDefault;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Settlement {

    private UUID owner;
    private String name;
    private String description;
    private String defaultRole;
    private Location location;

    private int maxLand;
    private int maxCitizens;

    private final List<Role> roles;

    public static Settlement create(Player player, String name) {
        return new Settlement(player, name, Arrays.stream(RoleDefault.values())
                .map(RoleDefault::build)
                .collect(Collectors.toList()));
    }

    public Settlement(Player player, String name, List<Role> roles) {
        this.owner = player.getUniqueId();
        this.name = name;
        this.roles = roles;
        this.maxCitizens = 10;
        this.maxLand = 15;
        this.description = "A peaceful place.";
        this.location = player.getLocation();
        this.defaultRole = RoleDefault.CITIZEN.name();
    }

    public void setOwner(OfflinePlayer offlinePlayer) {
        this.owner = offlinePlayer.getUniqueId();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    public void setMaxCitizens(int maxCitizens) {
        this.maxCitizens = maxCitizens;
    }

    public void setMaxLand(int maxLand) {
        this.maxLand = maxLand;
    }

    public boolean add(Role role) {
        return roles.add(role);
    }

    public void remove(Role role) {
        if(defaultRole.equalsIgnoreCase(role.getName())) return;
        roles.removeIf(r -> r.getName().equalsIgnoreCase(role.getName()));
    }

    public boolean isOwner(OfflinePlayer offlinePlayer) {
        return owner.equals(offlinePlayer.getUniqueId());
    }

    public boolean hasRole(String name) {
        return roles.stream().anyMatch(role -> role.getName().equalsIgnoreCase(name));
    }

    public Role getRole(String name) {
        return roles.stream().filter(role -> role.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Location getLocation() {
        return location;
    }

    public List<Role> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    public String getName() {
        return name;
    }

    public String getDefaultRole() {
        return defaultRole;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxLand() {
        return maxLand;
    }

    public int getMaxCitizens() {
        return maxCitizens;
    }
}
