package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.database.dao.MemberDao;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import org.bukkit.OfflinePlayer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public interface MemberService extends Service {

    boolean hasSettlement(OfflinePlayer offlinePlayer);

    void addMember(Member member);

    Member createMember(OfflinePlayer offlinePlayer);

    void addMember(OfflinePlayer offlinePlayer, Role role, Settlement settlement);

    void remove(OfflinePlayer offlinePlayer);

    void clean(Settlement settlement);

    void promote(Role role, Member member, List<Role> roles, Runnable runnable);

    void demote(Role role, Member member, List<Role> roles, Runnable runnable);

    int getCount();

    Member getMember(OfflinePlayer offlinePlayer);

    Set<Member> getMembers(Settlement settlement);

    Set<Member> getMembers(long settlementId);

    LinkedHashMap<Long, Long> getTop(int limit);

    Set<OfflinePlayer> getOfflinePlayers(Settlement settlement);

    Set<OfflinePlayer> getOfflinePlayers(long settlementId);

    void sync(Settlement settlement, Role role);

    MemberDao getDao();
}