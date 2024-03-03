package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import org.bukkit.OfflinePlayer;

import java.util.LinkedHashMap;
import java.util.List;

public interface MemberService extends ServiceInterface {

    boolean hasSettlement(OfflinePlayer offlinePlayer);

    void add(OfflinePlayer offlinePlayer, String name, String defaultRole);

    void remove(OfflinePlayer offlinePlayer);

    void clean(String settlementName);

    int getCount();

    Member getCitizen(OfflinePlayer offlinePlayer);

    List<Member> getMembers(String settlementName);

    LinkedHashMap<String, Long> getTop(int limit);

    List<OfflinePlayer> getOfflinePlayers(String settlementName);

    void sync(String settlementName, String defaultRole, Role role);
}