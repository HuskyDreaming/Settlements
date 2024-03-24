package com.huskydreaming.settlements.services.implementations;

import com.google.gson.reflect.TypeToken;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.Json;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MemberServiceImpl implements MemberService {

    private Map<UUID, Member> members = new ConcurrentHashMap<>();

    @Override
    public void serialize(HuskyPlugin plugin) {
        Json.write(plugin, "data/members", members);
        plugin.getLogger().info("Saved " + members.size() + " members(s).");
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        Type type = new TypeToken<Map<UUID, Member>>() {}.getType();
        members = Json.read(plugin, "data/members", type);
        if (members == null) members = new ConcurrentHashMap<>();

        int size = members.size();
        if (size > 0) {
            plugin.getLogger().info("Registered " + size + " members(s).");
        }
    }

    @Override
    public boolean hasSettlement(OfflinePlayer offlinePlayer) {
        return members.containsKey(offlinePlayer.getUniqueId());
    }

    @Override
    public void add(OfflinePlayer offlinePlayer, String name, String defaultRole) {
        members.put(offlinePlayer.getUniqueId(), Member.create(name, defaultRole));
    }

    @Override
    public void remove(OfflinePlayer offlinePlayer) {
        members.remove(offlinePlayer.getUniqueId());
    }

    @Override
    public void clean(String settlementName) {
        members.values().removeIf(member -> member.getSettlement().equalsIgnoreCase(settlementName));
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Member getCitizen(OfflinePlayer offlinePlayer) {
        return members.get(offlinePlayer.getUniqueId());
    }

    @Override
    public List<Member> getMembers(String settlementName) {
        return members.values().stream()
                .filter(member -> member.getSettlement().equalsIgnoreCase(settlementName))
                .collect(Collectors.toList());
    }

    @Override
    public LinkedHashMap<String, Long> getTop(int limit) {
        return members.values().stream()
                .collect(Collectors.groupingBy(Member::getSettlement, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public List<OfflinePlayer> getOfflinePlayers(String settlementName) {
        return members.entrySet().stream()
                .filter(entry -> entry.getValue().getSettlement().equalsIgnoreCase(settlementName))
                .map(entry -> Bukkit.getOfflinePlayer(entry.getKey()))
                .collect(Collectors.toList());
    }

    @Override
    public void sync(String settlementName, String defaultRole, Role role) {
        for (Member member : getMembers(settlementName)) {
            if (member.getRole().equalsIgnoreCase(role.getName())) {
                member.setRole(defaultRole);
            }
        }
    }
}