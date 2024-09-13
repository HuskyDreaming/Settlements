package com.huskydreaming.settlements.database.entities;

import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntity;
import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntityType;
import com.huskydreaming.settlements.database.SqlType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.UUID;

public class Settlement implements SqlEntity, Serializable {

    private long id;
    private String name;
    private String description;
    private String tag;
    private long roleId;
    private UUID ownerUUID;
    private UUID worldUID;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public Settlement() {

    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public SqlEntityType getEntityType() {
        return SqlType.SETTLEMENT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public boolean isOwner(Player player) {
        return ownerUUID.equals(player.getUniqueId());
    }

    public boolean isOwner(OfflinePlayer offlinePlayer) {
        return ownerUUID.equals(offlinePlayer.getUniqueId());
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public void setOwner(OfflinePlayer offlinePlayer) {
        this.ownerUUID = offlinePlayer.getUniqueId();
    }

    public UUID getWorldUID() {
        return worldUID;
    }

    public void setWorldUID(UUID worldUID) {
        this.worldUID = worldUID;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setLocation(Location location) {
        World world = location.getWorld();
        if(world != null) {
            this.worldUID = location.getWorld().getUID();
            this.x = location.getX();
            this.y = location.getY();
            this.z = location.getZ();
            this.yaw = location.getYaw();
            this.pitch = location.getPitch();
        }
    }

    public Location toLocation() {
        World world = Bukkit.getWorld(worldUID);
        if(world == null) return null;
        return  new Location(world, x, y, z, yaw, pitch);
    }
}
