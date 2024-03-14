package com.huskydreaming.settlements.persistence;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Objects;

public class Claim {

    private final String world;
    private final int x;
    private final int z;

    public static Claim deserialize(String[] strings) {
        return new Claim(strings[0], Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
    }

    public static Claim deserialize(Chunk chunk) {
        return new Claim(chunk);
    }

    public Claim(String world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public Claim(Chunk chunk) {
        this.world = chunk.getWorld().getName();
        this.x = chunk.getX();
        this.z = chunk.getZ();
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public Chunk toChunk() {
        World world = Bukkit.getWorld(this.world);
        if(world == null) return null;
        return world.getChunkAt(this.x, this.z);
    }

    @Override
    public String toString() {
        return world + ":" + x + ":" + z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Claim claim)) return false;
        return x == claim.x && z == claim.z && Objects.equals(world, claim.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, z);
    }
}
