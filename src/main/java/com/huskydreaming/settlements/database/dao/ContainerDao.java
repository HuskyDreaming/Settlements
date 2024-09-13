package com.huskydreaming.settlements.database.dao;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.abstraction.AbstractDao;
import com.huskydreaming.settlements.database.entities.Container;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContainerDao extends AbstractDao<Container> {

    public ContainerDao(HuskyPlugin plugin) {
        super(plugin);
    }

    @Override
    public int setStatement(PreparedStatement statement, Container container) throws SQLException {
        statement.setLong(1, container.getSettlementId());
        statement.setInt(2, container.getMaxClaims());
        statement.setInt(3, container.getMaxHomes());
        statement.setInt(4, container.getMaxMembers());
        statement.setInt(5, container.getMaxRoles());
        return 6;
    }

    @Override
    public Container fromResult(ResultSet result) throws SQLException {
        Container container = new Container();
        container.setId(result.getLong("id"));
        container.setSettlementId(result.getLong("settlement_id"));
        container.setMaxClaims(result.getInt("max_claims"));
        container.setMaxHomes(result.getInt("max_homes"));
        container.setMaxMembers(result.getInt("max_members"));
        container.setMaxRoles(result.getInt("max_roles"));
        return container;
    }
}