package com.huskydreaming.settlements.persistence.lands;

import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Land {

    private final int x;
    private final int z;
    private UUID uniqueId;

    public Land(Chunk chunk) {
        this.x = chunk.getX();
        this.z = chunk.getZ();
    }

    public static Land create(Chunk chunk) {
        return new Land(chunk);
    }

    public boolean matches(Chunk chunk) {
        return chunk.getX() == x && chunk.getZ() == z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
}
