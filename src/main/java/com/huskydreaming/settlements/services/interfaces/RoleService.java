package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.services.base.ServiceInterface;

import java.util.List;

public interface RoleService extends ServiceInterface {

    void setup(Settlement settlement);

    void remove(Settlement settlement, Role role);

    void add(Settlement settlement, String name);

    boolean promote(Settlement settlement, Member member);

    boolean demote(Settlement settlement, Member member);

    int getIndex(Settlement settlement, Member member);

    int getIndex(Settlement settlement, String name);

    Role sync(List<Role> roles, Settlement settlement, Member member);

    Role getRole(Settlement settlement, Member member);

    List<Role> getRoles(Settlement settlement);
}
