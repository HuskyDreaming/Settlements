package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.database.dao.RoleDao;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;

import java.util.List;
import java.util.Set;

public interface RoleService extends Service {

    Role getRole(Settlement settlement, String roleName);

    Role getOtherRole(Settlement settlement, String roleName);

    boolean hasRole(Settlement settlement, String name);

    void clean(Settlement settlement);
    Role createDefaultRole();

    void addRole(Role role);

    void addRole(Settlement settlement, String roleName, int weight);

    void removeRole(Settlement settlement, String roleName);

    Set<Role> getRoles(Settlement settlement);

    List<Role> getRoleList(Settlement settlement);

    Role getRole(long id);

    Role getRole(Member member);

    int getIndex(Settlement settlement, Role role);

    Role sync(Member member, Settlement settlement);

    RoleDao getDao();
}