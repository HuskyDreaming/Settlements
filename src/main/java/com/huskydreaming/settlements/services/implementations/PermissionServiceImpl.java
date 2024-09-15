package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.implementations.RepositoryImpl;
import com.huskydreaming.huskycore.interfaces.Repository;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.SqlType;
import com.huskydreaming.settlements.database.dao.PermissionDao;
import com.huskydreaming.settlements.database.entities.Permission;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.PermissionService;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PermissionServiceImpl implements PermissionService {

    private final PermissionDao permissionDao;
    private final Repository<Permission> permissionRepository;

    public PermissionServiceImpl(SettlementPlugin plugin) {
        this.permissionRepository = new RepositoryImpl<>();
        this.permissionDao = new PermissionDao(plugin);
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        permissionDao.bulkUpdate(SqlType.PERMISSION, permissionRepository.values());
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        permissionDao.bulkImport(SqlType.PERMISSION, permissionRepository::bulkAdd);
    }

    @Override
    public void clean(Set<Role> roles) {
        Set<Long> permissionIds = permissionRepository.entries().stream()
                .filter(e -> {
                    Role role = roles.stream().filter(r -> r.getId() == e.getValue().getRoleId()).findFirst().orElse(null);
                    if(role == null) return false;
                    return role.getId() == e.getValue().getRoleId();
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        permissionDao.bulkDelete(SqlType.PERMISSION, permissionIds);
        permissionRepository.keys().removeAll(permissionIds);
    }

    @Override
    public Permission createPermission(PermissionType permissionType) {
        Permission permission = new Permission();
        permission.setPermissionType(permissionType.toString());
        return permission;
    }

    @Override
    public void addPermission(Permission permission) {
        permissionRepository.add(permission);
    }

    @Override
    public void addPermission(Role role, PermissionType permissionType, Consumer<Long> consumer) {
        Permission permission = new Permission();
        permission.setRoleId(role.getId());
        permission.setPermissionType(permissionType.toString());

        permissionDao.insert(permission).queue(i -> {
            permission.setId(i);
            permissionRepository.add(permission);
            consumer.accept(i);
        });
    }

    @Override
    public void removePermission(Permission permission, Consumer<Boolean> consumer) {
        permissionDao.delete(permission).queue(success -> {
            if(success) {
                permissionRepository.remove(permission);
                consumer.accept(true);
            }
        });
    }

    @Override
    public Permission getPermissions(Role role, PermissionType permissionType) {
        return permissionRepository.values().stream()
                .filter(p -> p.getRoleId() == role.getId() && p.getPermissionType().equalsIgnoreCase(permissionType.toString()))
                .findFirst().orElse(null);
    }

    @Override
    public Set<PermissionType> getPermissions(Role role) {
        return permissionRepository.values().stream().filter(p -> p.getRoleId() == role.getId())
                .map(p -> PermissionType.valueOf(p.getPermissionType().toUpperCase()))
                .collect(Collectors.toSet());
    }

    @Override
    public PermissionDao getDao() {
        return permissionDao;
    }
}
