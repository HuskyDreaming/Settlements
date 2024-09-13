package com.huskydreaming.settlements.database.dao;

import com.huskydreaming.huskycore.abstraction.AbstractDao;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.entities.Home;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class HomeDao extends AbstractDao<Home> {

    public HomeDao(SettlementPlugin plugin) {
        super(plugin);
    }

    @Override
    public int setStatement(PreparedStatement statement, Home home) throws SQLException {
        statement.setString(1, home.getName());
        statement.setLong(2, home.getSettlementId());
        statement.setString(3, home.getWorldUID().toString());
        statement.setDouble(4, home.getX());
        statement.setDouble(5, home.getY());
        statement.setDouble(6, home.getZ());
        statement.setFloat(7, home.getYaw());
        statement.setFloat(8, home.getPitch());
        return 9;
    }

    @Override
    public Home fromResult(ResultSet result) throws SQLException {
        Home home = new Home();
        home.setId(result.getLong("id"));
        home.setSettlementId(result.getLong("settlement_id"));
        home.setName(result.getString("name"));
        home.setWorldUID(UUID.fromString(result.getString("world_uid")));
        home.setX(result.getDouble("x"));
        home.setY(result.getDouble("y"));
        home.setZ(result.getDouble("z"));
        home.setYaw(result.getFloat("yaw"));
        home.setPitch(result.getFloat("pitch"));
        return home;
    }
}