package com.huskydreaming.settlements.database;

import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntityType;

public enum SqlType implements SqlEntityType {
    CLAIM("claim", "settlement_id", "world_uid", "x", "z"),
    CONTAINER("container", "settlement_id", "max_claims", "max_homes", "max_members", "max_roles"),
    FLAG("flag", "type", "settlement_id"),
    HOME("home", "name", "settlement_id", "world_uid", "x", "y", "z", "yaw", "pitch"),
    MEMBER("member", "player_uuid", "settlement_id", "role_id", "last_online", "auto_claim"),
    PERMISSION("permission", "type", "role_id"),
    ROLE("role", "name", "weight", "settlement_id"),
    SETTLEMENT("settlement", "name", "description", "tag", "role_id", "owner_uuid", "world_uid", "x", "y", "z", "yaw", "pitch"),
    TRUST("trust", "player_uuid", "settlement_id");

    private final String table;
    private final String[] columns;

    SqlType(String table, String... columns) {
        this.table = table;
        this.columns = columns;
    }

    @Override
    public String toTable() {
        return table;
    }
    @Override
    public String[] toColumns() {
        return columns;
    }
}