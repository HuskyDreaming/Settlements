package com.huskydreaming.settlements.database.persistence;

import com.huskydreaming.settlements.enumeration.FlagType;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.enumeration.types.NotificationType;
import com.huskydreaming.settlements.enumeration.types.SettlementDefaultType;
import org.bukkit.World;

import java.util.Collections;
import java.util.Map;
import java.util.List;

public class Config {
    private boolean trusting;
    private boolean teleportation;
    private boolean homes;
    private String localization;
    private String emptyPlaceholder;
    private NotificationType notificationType;
    private List<String> disabledWorlds;
    private List<FlagType> flags;
    private Map<String, List<PermissionType>> defaultRoles;
    private Map<SettlementDefaultType, Integer> settlementDefaults;

    public boolean containsDisableWorld(World world) {
        return disabledWorlds.contains(world.getName());
    }

    public void addDisabledWorld(World world) {
        disabledWorlds.add(world.getName());
    }

    public void removeDisabledWorld(World world) {
        disabledWorlds.remove(world.getName());
    }

    public void setDisabledWorlds(List<String> disabledWorlds) {
        this.disabledWorlds = disabledWorlds;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getEmptyPlaceholder() {
        return emptyPlaceholder;
    }

    public void setEmptyPlaceholder(String emptyPlaceholder) {
        this.emptyPlaceholder = emptyPlaceholder;
    }

    public Map<String, List<PermissionType>> getDefaultRoles() {
        return Collections.unmodifiableMap(defaultRoles);
    }

    public void setDefaultRoles(Map<String, List<PermissionType>> defaultRoles) {
        this.defaultRoles = defaultRoles;
    }

    public Integer getSettlementDefault(SettlementDefaultType type) {
        return settlementDefaults.get(type);
    }

    public void setSettlementDefault(SettlementDefaultType type, int value) {
        settlementDefaults.put(type, value);
    }

    public void setSettlementDefaults(Map<SettlementDefaultType, Integer> settlementDefaults) {
        this.settlementDefaults = settlementDefaults;
    }

    public List<FlagType> getFlags() {
        return Collections.unmodifiableList(flags);
    }

    public void setFlags(List<FlagType> flags) {
        this.flags = flags;
    }

    public boolean isTrusting() {
        return trusting;
    }

    public void setTrusting(boolean trusting) {
        this.trusting = trusting;
    }

    public boolean isTeleportation() {
        return teleportation;
    }

    public boolean isHomes() {
        return homes;
    }

    public void setHomes(boolean homes) {
        this.homes = homes;
    }

    public void setTeleportation(boolean teleportation) {
        this.teleportation = teleportation;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}
