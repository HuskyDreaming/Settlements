package com.huskydreaming.settlements.database.entities;

import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntity;
import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntityType;
import com.huskydreaming.settlements.database.SqlType;

import java.io.Serializable;

public class Role implements SqlEntity, Serializable  {

    private long id;
    private String name;
    private int weight;
    private long settlementId;

    public Role() {

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
        return SqlType.ROLE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setSettlementId(long settlementId) {
        this.settlementId = settlementId;
    }

    public long getSettlementId() {
        return settlementId;
    }
}
