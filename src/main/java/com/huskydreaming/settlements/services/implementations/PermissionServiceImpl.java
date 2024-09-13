package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.SqlType;
import com.huskydreaming.settlements.database.dao.PermissionDao;
import com.huskydreaming.settlements.database.entities.Permission;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.PermissionService;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PermissionServiceImpl implements PermissionService {

    private final PermissionDao permissionDao;
    private final Map<Long, Permission> permissions;

    public PermissionServiceImpl(SettlementPlugin plugin) {
        this.permissions = new ConcurrentHashMap<>();
        this.permissionDao = new PermissionDao(plugin);
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        permissionDao.bulkUpdate(SqlType.PERMISSION, permissions.values());
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        permissionDao.bulkImport(SqlType.PERMISSION, permissions::putAll);
    }

    @Override
    public Permission createPermission(PermissionType permissionType) {
        Permission permission = new Permission();
        permission.setPermissionType(permissionType.toString());
        return permission;
    }

    @Override
    public void addPermission(Permission permission) {
        permissions.put(permission.getId(), permission);
    }

    @Override
    public void addPermission(Role role, PermissionType permissionType, Consumer<Long> consumer) {
        Permission permission = new Permission();
        permission.setRoleId(role.getId());
        permission.setPermissionType(permissionType.toString());

        permissionDao.insert(permission).queue(i -> {
            permission.setId(i);
            permissions.put(i, permission);
            consumer.accept(i);
        });
    }

    @Override
    public void removePermission(Permission permission, Consumer<Long> consumer) {
        permissionDao.delete(permission).queue(i -> {
            permissions.remove(permission.getId());
            consumer.accept(i);
        });
    }

    @Override
    public Permission getPermissions(Role role, PermissionType permissionType) {
        return permissions.values().stream()
                .filter(p -> p.getRoleId() == role.getId() && p.getPermissionType().equalsIgnoreCase(permissionType.toString()))
                .findFirst().orElse(null);
    }

    @Override
    public Set<PermissionType> getPermissions(Role role) {
        return permissions.values().stream().filter(p -> p.getRoleId() == role.getId())
                .map(p -> PermissionType.valueOf(p.getPermissionType().toUpperCase()))
                .collect(Collectors.toSet());
    }

    @Override
    public PermissionDao getDao() {
        return permissionDao;
    }
}
