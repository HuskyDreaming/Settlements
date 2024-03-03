package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.services.base.ServiceInterface;

import java.util.List;

public interface RoleService extends ServiceInterface {

    Role getOtherRole(String settlementName, String name);

    void remove(String settlementName, Role role);

    void add(String settlementName, String name);

    boolean promote(String settlementName, Role role, Member member);

    boolean demote(String settlementName, Role role, Member member);

    boolean hasRole(String settlementName, String name);

    int getIndex(String settlementName, Member member);

    void clean(String settlementName);

    void setup(String settlementName, Settlement settlement);

    List<Role> getRoles(String settlementName);

    Role getRole(Member member);

    Role getRole(String settlement, String name);

    int getIndex(String settlement, String name);

    Role sync(Member member, String defaultRole);
}