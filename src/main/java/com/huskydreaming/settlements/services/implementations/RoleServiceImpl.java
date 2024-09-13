package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.SqlType;
import com.huskydreaming.settlements.database.dao.RoleDao;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.services.interfaces.RoleService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;
    private final Map<Long, Role> roles;


    public RoleServiceImpl(SettlementPlugin plugin) {
        this.roles = new ConcurrentHashMap<>();
        this.roleDao = new RoleDao(plugin);
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        roleDao.bulkImport(SqlType.ROLE, roles::putAll);
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        roleDao.bulkUpdate(SqlType.ROLE, roles.values());
    }

    @Override
    public void clean(Settlement settlement) {
        Set<Long> roleIds = roles.entrySet().stream().filter(r -> r.getValue().getSettlementId() == settlement.getId())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        roleDao.bulkDelete(SqlType.ROLE, roleIds);
        roles.keySet().removeAll(roleIds);
    }

    @Override
    public Role createDefaultRole() {
        Role role = new Role();
        role.setName("Citizen");
        role.setWeight(1);
        return role;
    }


    @Override
    public void addRole(Role role) {
        roles.put(role.getId(), role);
    }

    @Override
    public void addRole(Settlement settlement, String roleName, int weight) {
        Role role = new Role();
        role.setName(roleName);
        role.setWeight(weight);
        role.setSettlementId(settlement.getId());

        roleDao.insert(role).queue(i -> {
            role.setId(i);
            roles.put(i, role);
        });
    }

    @Override
    public void removeRole(Settlement settlement, String roleName) {
        Role role = roles.values().stream()
                .filter(r -> r.getSettlementId() == settlement.getId() && r.getName().equalsIgnoreCase(roleName)).findFirst().orElse(null);

        roleDao.delete(role).queue(roles::remove);
    }

    @Override
    public Set<Role> getRoles(Settlement settlement) {
        return roles.values().stream()
                .filter(r -> r.getSettlementId() == settlement.getId())
                .collect(Collectors.toSet());
    }

    @Override
    public List<Role> getRoleList(Settlement settlement) {
        return roles.values().stream()
                .filter(r -> r.getSettlementId() == settlement.getId())
                .collect(Collectors.toList());
    }


    @Override
    public Role getRole(long id) {
        return roles.get(id);
    }

    @Override
    public Role getRole(Member member) {
        return roles.get(member.getRoleId());
    }

    @Override
    public Role getRole(Settlement settlement, String roleName) {
        return roles.values().stream()
                .filter(r -> r.getSettlementId() == settlement.getId() && r.getName().equalsIgnoreCase(roleName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Role getOtherRole(Settlement settlement, String roleName) {
        return roles.values().stream()
                .filter(r -> r.getSettlementId() == settlement.getId() && !(r.getName().equalsIgnoreCase(roleName)))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean hasRole(Settlement settlement, String name) {
        return roles.values().stream().anyMatch(r -> r.getSettlementId() == settlement.getId() && r.getName().equalsIgnoreCase(name));
    }

    @Override
    public int getIndex(Settlement settlement, Role role) {
        List<Role> list = roles.values().stream()
                .filter(r -> r.getSettlementId() == settlement.getId())
                .toList();
        return list.indexOf(role);
    }


    @Override
    public Role sync(Member member, Settlement settlement) {
        Set<Role> roleSet = roles.values().stream()
                .filter(r -> r.getSettlementId() == member.getSettlementId())
                .collect(Collectors.toSet());

        Predicate<Role> rolePredicate = (r -> r.getId() == member.getRoleId());

        if (roleSet.stream().noneMatch(rolePredicate)) {
            member.setRoleId(settlement.getRoleId());
        }

        return roles.values().stream().filter(rolePredicate).findFirst().orElse(null);
    }

    @Override
    public RoleDao getDao() {
        return roleDao;
    }
}