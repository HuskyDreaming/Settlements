package com.huskydreaming.settlements.database.dao;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.abstraction.AbstractDao;
import com.huskydreaming.settlements.database.entities.Flag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FlagDao extends AbstractDao<Flag> {

    public FlagDao(HuskyPlugin plugin) {
        super(plugin);
    }

    @Override
    public int setStatement(PreparedStatement statement, Flag flag) throws SQLException {
        statement.setString(1, flag.getType());
        statement.setLong(2, flag.getSettlementId());
        return 3;
    }

    @Override
    public Flag fromResult(ResultSet result) throws SQLException {
        Flag flag = new Flag();
        flag.setId(result.getLong("id"));
        flag.setType(result.getString("type"));
        flag.setSettlementId(result.getLong("settlement_id"));
        return flag;
    }
}
