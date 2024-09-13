package com.huskydreaming.settlements.database.entities;

import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntity;
import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntityType;
import com.huskydreaming.settlements.database.SqlType;

import java.io.Serializable;
import java.util.UUID;

public class Member implements SqlEntity, Serializable {

    private long id;
    private UUID uuid;
    private long settlementId;
    private long roleId;

    private String lastOnline;
    private boolean autoClaim;

    public Member() {

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
        return SqlType.MEMBER;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public void setUniqueId(UUID uuid) {
        this.uuid = uuid;
    }

    public long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(long settlementId) {
        this.settlementId = settlementId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    public boolean isAutoClaim() {
        return autoClaim;
    }

    public void setAutoClaim(boolean autoClaim) {
        this.autoClaim = autoClaim;
    }
}