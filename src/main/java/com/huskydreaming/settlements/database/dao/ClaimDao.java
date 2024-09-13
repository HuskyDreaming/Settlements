package com.huskydreaming.settlements.database.dao;

import com.huskydreaming.huskycore.abstraction.AbstractDao;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.entities.Claim;

import java.sql.*;
import java.util.UUID;

public class ClaimDao extends AbstractDao<Claim> {

    public ClaimDao(SettlementPlugin plugin) {
        super(plugin);
    }


    @Override
    public int setStatement(PreparedStatement statement, Claim claim) throws SQLException {
        statement.setLong(1, claim.getSettlementId());
        statement.setString(2, claim.getWorldUID().toString());
        statement.setInt(3, claim.getX());
        statement.setInt(4, claim.getZ());
        return 5;
    }

    @Override
    public Claim fromResult(ResultSet result) throws SQLException {
        Claim claim = new Claim();
        claim.setId(result.getLong("id"));
        claim.setSettlementId(result.getLong("settlement_id"));
        claim.setWorldUID(UUID.fromString(result.getString("world_uid")));
        claim.setX(result.getInt("x"));
        claim.setZ(result.getInt("z"));
        return claim;
    }
}