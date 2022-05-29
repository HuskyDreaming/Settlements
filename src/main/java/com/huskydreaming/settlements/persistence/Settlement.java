package com.huskydreaming.settlements.persistence;

import com.huskydreaming.settlements.persistence.lands.Land;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RoleDefault;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Settlement {

    private UUID owner;
    private String name;
    private String world;
    private String description;
    private String defaultRole;
    private Location location;

    private int maxLand;
    private int maxCitizens;

    private final List<Role> roles;
    private final Set<Land> lands;
    private final Map<UUID, String> citizens;

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
        this.lands = new HashSet<>();
        this.description = "A peaceful place.";
        this.citizens = new ConcurrentHashMap<>();
        this.location = player.getLocation();
        this.defaultRole = RoleDefault.CITIZEN.name();

        World world = location.getWorld();
        if(world != null) {
            this.world = location.getWorld().getName();
        }

        add(player.getLocation().getChunk());
        citizens.put(player.getUniqueId(), RoleDefault.MAJESTY.name());
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

    public void setRole(Player player, Role role) {
        citizens.put(player.getUniqueId(), role.getName());
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

    public void add(Chunk chunk) {
        lands.add(Land.create(chunk));
    }

    public void add(Player player) {
        citizens.put(player.getUniqueId(), defaultRole);
    }

    public boolean add(Role role) {
        return roles.add(role);
    }

    public void remove(OfflinePlayer offlinePlayer) {
        citizens.remove(offlinePlayer.getUniqueId());
    }

    public void remove(Chunk chunk) {
        lands.removeIf(land -> land.matches(chunk));
    }

    public void remove(Role role) {
        if(defaultRole.equalsIgnoreCase(role.getName())) return;
        roles.removeIf(r -> r.getName().equalsIgnoreCase(role.getName()));
    }

    public boolean isClaimed(Chunk chunk) {
        return lands.stream().anyMatch(land -> land.matches(chunk));
    }

    public boolean isCitizen(Player player) {
        return citizens.containsKey(player.getUniqueId());
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

    public Role getRole(OfflinePlayer offlinePlayer) {
        AtomicReference<String> atomicReference = new AtomicReference<>(citizens.get(offlinePlayer.getUniqueId()));
        boolean isValid = roles.stream().anyMatch(role -> role.getName().equalsIgnoreCase(atomicReference.get()));

        if(!isValid) {
            citizens.put(offlinePlayer.getUniqueId(), defaultRole);
            atomicReference.set(citizens.get(offlinePlayer.getUniqueId()));
        }
        Predicate<Role> rolePredicate = r -> r.getName().equalsIgnoreCase(atomicReference.get());

        return roles.stream().filter(rolePredicate).findFirst().orElse(null);
    }

    public String getWorld() {
        return world;
    }

    public Location getLocation() {
        return location;
    }

    public OfflinePlayer[] getCitizens() {
        return citizens.keySet().stream().map(Bukkit::getOfflinePlayer).toArray(OfflinePlayer[]::new);
    }

    public List<Role> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    public Set<Land> getLands() {
        return Collections.unmodifiableSet(lands);
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
