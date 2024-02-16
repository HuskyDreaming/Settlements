package com.huskydreaming.settlements.services.implementations;

import com.google.gson.reflect.TypeToken;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.storage.Json;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MemberServiceImpl implements MemberService {

    private Map<UUID, Member> members = new ConcurrentHashMap<>();

    @Override
    public boolean hasSettlement(OfflinePlayer offlinePlayer) {
        return members.containsKey(offlinePlayer.getUniqueId());
    }

    @Override
    public void add(OfflinePlayer offlinePlayer, Settlement settlement) {
        members.put(offlinePlayer.getUniqueId(), Member.create(settlement, settlement.getDefaultRole()));
    }

    @Override
    public void remove(OfflinePlayer offlinePlayer) {
        members.remove(offlinePlayer.getUniqueId());
    }

    @Override
    public void clean(Settlement settlement) {
        members.values().removeIf(member -> member.getSettlement().equalsIgnoreCase(settlement.getName()));
    }

    @Override
    public Member getCitizen(OfflinePlayer offlinePlayer) {
        return members.get(offlinePlayer.getUniqueId());
    }

    @Override
    public List<Member> getMembers(Settlement settlement) {
        return members.values().stream()
                .filter(member -> member.getSettlement().equalsIgnoreCase(settlement.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<OfflinePlayer> getOfflinePlayers(Settlement settlement) {
        return members.entrySet().stream()
                .filter(entry -> entry.getValue().getSettlement().equalsIgnoreCase(settlement.getName()))
                .map(entry -> Bukkit.getOfflinePlayer(entry.getKey()))
                .collect(Collectors.toList());
    }

    @Override
    public void sync(Settlement settlement, Role role) {
        for(Member member : getMembers(settlement)) {
            if(member.getRole().equalsIgnoreCase(role.getName())) {
                member.setRole(settlement.getDefaultRole());
            }
        }
    }

    @Override
    public void serialize(SettlementPlugin plugin) {
        Json.write(plugin, "data/members", members);
    }

    @Override
    public void deserialize(SettlementPlugin plugin) {
        Type type = new TypeToken<Map<UUID, Member>>(){}.getType();
        members = Json.read(plugin, "data/members", type);
        if(members == null) members = new ConcurrentHashMap<>();

        int size = members.size();
        if(size > 0) {
            plugin.getLogger().info("Registered " + size + " members(s).");
        }
    }
}