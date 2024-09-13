package com.huskydreaming.settlements.database.dao;

import com.huskydreaming.huskycore.abstraction.AbstractDao;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.entities.Settlement;

import java.sql.*;
import java.util.UUID;

public class SettlementDao extends AbstractDao<Settlement> {

    public SettlementDao(SettlementPlugin plugin) {
        super(plugin);
    }

    @Override
    public int setStatement(PreparedStatement statement, Settlement settlement) throws SQLException {
        statement.setString(1, settlement.getName());
        statement.setString(2, settlement.getDescription());
        statement.setString(3, settlement.getTag());
        statement.setLong(4, settlement.getRoleId());
        statement.setString(5, settlement.getOwnerUUID().toString());
        statement.setString(6, settlement.getWorldUID().toString());
        statement.setDouble(7, settlement.getX());
        statement.setDouble(8, settlement.getY());
        statement.setDouble(9, settlement.getZ());
        statement.setFloat(10, settlement.getYaw());
        statement.setFloat(11, settlement.getPitch());
        return 12;
    }

    @Override
    public Settlement fromResult(ResultSet result) throws SQLException {
        Settlement settlement = new Settlement();
        settlement.setId(result.getLong("id"));
        settlement.setName(result.getString("name"));
        settlement.setDescription(result.getString("description"));
        settlement.setRoleId(result.getLong("role_id"));
        settlement.setOwnerUUID(UUID.fromString(result.getString("owner_uuid")));
        settlement.setWorldUID(UUID.fromString(result.getString("world_uid")));
        settlement.setX(result.getDouble("x"));
        settlement.setY(result.getDouble("y"));
        settlement.setZ(result.getDouble("z"));
        settlement.setYaw(result.getFloat("yaw"));
        settlement.setPitch(result.getFloat("pitch"));
        return settlement;
    }
}