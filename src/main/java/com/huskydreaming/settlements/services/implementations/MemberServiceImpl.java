package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.SqlType;
import com.huskydreaming.settlements.database.dao.MemberDao;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MemberServiceImpl implements MemberService {

    private final MemberDao memberDao;
    private final Map<Long, Member> members;

    public MemberServiceImpl(SettlementPlugin plugin) {
        memberDao = new MemberDao(plugin);
        members = new ConcurrentHashMap<>();
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        memberDao.bulkImport(SqlType.MEMBER, members::putAll);
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        memberDao.bulkUpdate(SqlType.MEMBER, members.values());
    }

    @Override
    public boolean hasSettlement(OfflinePlayer offlinePlayer) {
        return members.values().stream().anyMatch(m -> m.getUniqueId().equals(offlinePlayer.getUniqueId()));
    }

    @Override
    public void addMember(Member member) {
        members.put(member.getId(), member);
    }

    @Override
    public Member createMember(OfflinePlayer offlinePlayer) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String lastOnline = df.format(today);

        Member member = new Member();
        member.setLastOnline(lastOnline);
        member.setUniqueId(offlinePlayer.getUniqueId());
        member.setAutoClaim(false);
        return member;
    }


    @Override
    public void addMember(OfflinePlayer offlinePlayer, Role role, Settlement settlement) {
        Member member = new Member();
        member.setUniqueId(offlinePlayer.getUniqueId());
        member.setRoleId(role.getId());
        member.setSettlementId(settlement.getId());
        member.setAutoClaim(false);

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String lastOnline = df.format(today);
        member.setLastOnline(lastOnline);

        memberDao.insert(member).queue(i -> {
            member.setId(i);
            members.put(i, member);
        });
    }

    @Override
    public void remove(OfflinePlayer offlinePlayer) {
        Predicate<Member> memberPredicate = (m -> m.getUniqueId().equals(offlinePlayer.getUniqueId()));
        Member member = members.values().stream().filter(memberPredicate).findFirst().orElse(null);

        if(member != null) {
            memberDao.delete(member).queue();
            members.remove(member.getId());
        }
    }

    @Override
    public void clean(Settlement settlement) {
        Set<Long> memberIds = members.entrySet().stream()
                .filter(e -> e.getValue().getSettlementId() == settlement.getId())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        memberDao.bulkDelete(SqlType.MEMBER, memberIds);
        members.keySet().removeAll(memberIds);
    }

    @Override
    public void promote(Role role, Member member, List<Role> roles, Runnable runnable) {
        int index = roles.indexOf(role);
        if (index < roles.size() - 1) {
            role = roles.get(index + 1);
            if (role != null) {
                long id = role.getId();
                member.setRoleId(id);
                runnable.run();
            }
        }
    }

    @Override
    public void demote(Role role, Member member, List<Role> roles, Runnable runnable) {
        int index = roles.indexOf(role);
        if (index >= 1) {
            role = roles.get(index - 1);
            if (role != null) {
                long id = role.getId();
                member.setRoleId(id);
                runnable.run();
            }
        }
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Member getMember(OfflinePlayer offlinePlayer) {
        return members.values().stream()
                .filter(m -> m.getUniqueId().equals(offlinePlayer.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Set<Member> getMembers(Settlement settlement) {
        return members.values().stream()
                .filter(m -> m.getSettlementId() == settlement.getId())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Member> getMembers(long settlementId) {
        return members.values().stream()
                .filter(m -> m.getSettlementId() == settlementId)
                .collect(Collectors.toSet());
    }

    @Override
    public LinkedHashMap<Long, Long> getTop(int limit) {
        return members.values().stream()
                .collect(Collectors.groupingBy(Member::getSettlementId, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public Set<OfflinePlayer> getOfflinePlayers(Settlement settlement) {
        return members.values().stream()
                .filter(m -> m.getSettlementId() == settlement.getId())
                .map(e -> Bukkit.getOfflinePlayer(e.getUniqueId()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<OfflinePlayer> getOfflinePlayers(long settlementId) {
        return members.values().stream()
                .filter(m -> m.getSettlementId() == settlementId)
                .map(e -> Bukkit.getOfflinePlayer(e.getUniqueId()))
                .collect(Collectors.toSet());
    }

    @Override
    public void sync(Settlement settlement, Role role) {
        for (Member member : getMembers(settlement)) {
            if (member.getRoleId() == role.getId()) {
                member.setRoleId(settlement.getRoleId());
            }
        }
    }

    @Override
    public MemberDao getDao() {
        return memberDao;
    }
}