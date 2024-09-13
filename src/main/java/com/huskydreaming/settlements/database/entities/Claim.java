package com.huskydreaming.settlements.database.entities;

import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntity;
import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntityType;
import com.huskydreaming.settlements.database.SqlType;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.io.Serializable;
import java.util.UUID;

public class Claim implements SqlEntity, Serializable  {

    private long id;
    private long settlementId;
    private UUID worldUID;
    private int x;
    private int z;

    public Claim() {

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
        return SqlType.CLAIM;
    }

    public long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(long settlementId) {
        this.settlementId = settlementId;
    }

    public UUID getWorldUID() {
        return worldUID;
    }

    public void setWorldUID(UUID worldUID) {
        this.worldUID = worldUID;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public Chunk toChunk() {
        World world = Bukkit.getWorld(worldUID);
        if(world == null) return null;
        return world.getChunkAt(x, z);
    }
}