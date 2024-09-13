package com.huskydreaming.settlements.database.dao;

import com.huskydreaming.huskycore.abstraction.AbstractDao;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.entities.Role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class RoleDao extends AbstractDao<Role> {

    public RoleDao(SettlementPlugin plugin) {
        super(plugin);
    }

    @Override
    public int setStatement(PreparedStatement statement, Role role) throws SQLException {
        statement.setString(1, role.getName());
        statement.setInt(2, role.getWeight());
        statement.setLong(3, role.getSettlementId());
        return 4;
    }

    @Override
    public Role fromResult(ResultSet result) throws SQLException {
        Role role = new Role();
        role.setId(result.getLong("id"));
        role.setName(result.getString("name"));
        role.setWeight(result.getInt("weight"));
        role.setSettlementId(result.getLong("settlement_id"));
        return role;
    }
}
