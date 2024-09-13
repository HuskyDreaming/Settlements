package com.huskydreaming.settlements.database.entities;

import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntity;
import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntityType;
import com.huskydreaming.settlements.database.SqlType;

import java.io.Serializable;
import java.util.UUID;

public class Trust implements SqlEntity, Serializable {

    private long id;
    private long settlementId;
    private UUID uuid;

    public Trust() {

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
        return SqlType.TRUST;
    }

    public long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(long settlementId) {
        this.settlementId = settlementId;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public void setUniqueId(UUID uuid) {
        this.uuid = uuid;
    }
}
