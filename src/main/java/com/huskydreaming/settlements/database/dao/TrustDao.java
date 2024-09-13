package com.huskydreaming.settlements.database.dao;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.abstraction.AbstractDao;
import com.huskydreaming.settlements.database.entities.Trust;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TrustDao extends AbstractDao<Trust> {

    public TrustDao(HuskyPlugin plugin) {
        super(plugin);
    }

    @Override
    public int setStatement(PreparedStatement statement, Trust trust) throws SQLException {
        statement.setString(1, trust.getUniqueId().toString());
        statement.setLong(2, trust.getSettlementId());
        return 3;
    }

    @Override
    public Trust fromResult(ResultSet result) throws SQLException {
        Trust trust = new Trust();
        trust.setId(result.getLong("id"));
        trust.setSettlementId(result.getLong("settlement_id"));
        trust.setUniqueId(UUID.fromString(result.getString("player_uuid")));
        return trust;
    }
}