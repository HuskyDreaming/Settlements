package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.database.dao.PermissionDao;
import com.huskydreaming.settlements.database.entities.Permission;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.enumeration.PermissionType;

import java.util.Set;
import java.util.function.Consumer;

public interface PermissionService extends Service {

    void clean(Set<Role> roles);

    Permission createPermission(PermissionType permissionType);

    void addPermission(Permission permission);

    void addPermission(Role role, PermissionType permissionType, Consumer<Long> consumer);

    void removePermission(Permission permission, Consumer<Boolean> consumer);

    Permission getPermissions(Role role, PermissionType permissionType);

    Set<PermissionType> getPermissions(Role role);

    PermissionDao getDao();
}
