package com.huskydreaming.settlements.database.dao;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.abstraction.AbstractDao;
import com.huskydreaming.settlements.database.entities.Permission;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PermissionDao extends AbstractDao<Permission> {

    public PermissionDao(HuskyPlugin plugin) {
        super(plugin);
    }

    @Override
    public int setStatement(PreparedStatement statement, Permission permission) throws SQLException {
        statement.setString(1, permission.getPermissionType());
        statement.setLong(2, permission.getRoleId());
        return 3;
    }

    @Override
    public Permission fromResult(ResultSet result) throws SQLException {
        Permission permission = new Permission();
        permission.setId(result.getLong("id"));
        permission.setPermissionType(result.getString("type"));
        permission.setRoleId(result.getLong("role_id"));
        return permission;
    }
}
