package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.SqlType;
import com.huskydreaming.settlements.database.dao.TrustDao;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.database.entities.Trust;
import com.huskydreaming.settlements.services.interfaces.TrustService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TrustServiceImpl implements TrustService {

    private final TrustDao trustDao;
    private final Map<Long, Trust> trusts;

    public TrustServiceImpl(SettlementPlugin plugin) {
        this.trusts = new ConcurrentHashMap<>();
        this.trustDao = new TrustDao(plugin);
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        trustDao.bulkImport(SqlType.TRUST, trusts::putAll);
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        trustDao.bulkUpdate(SqlType.TRUST, trusts.values());
    }

    @Override
    public void clean(Settlement settlement) {
        Set<Long> keys = trusts.entrySet().stream()
                .filter(e -> e.getValue().getSettlementId() == settlement.getId())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        trustDao.bulkDelete(SqlType.TRUST, keys);
        trusts.keySet().removeAll(keys);
    }

    @Override
    public void trust(OfflinePlayer offlinePlayer, Settlement settlement) {
        Trust trust = new Trust();
        trust.setUniqueId(offlinePlayer.getUniqueId());
        trust.setSettlementId(settlement.getId());

        trustDao.insert(trust).queue(i -> {
            trust.setId(i);
            trusts.put(i, trust);
        });
    }

    @Override
    public void unTrust(OfflinePlayer offlinePlayer, Settlement settlement) {
        unTrust(offlinePlayer, settlement.getRoleId());
    }

    @Override
    public void unTrust(OfflinePlayer offlinePlayer, long settlementId) {
        Predicate<Trust> trustPredicate = (t -> t.getSettlementId() == settlementId && t.getUniqueId().equals(offlinePlayer.getUniqueId()));
        Trust trust = trusts.values().stream().filter(trustPredicate).findFirst().orElse(null);

        if(trust != null) {
            trustDao.delete(trust).queue();
            trusts.remove(trust.getId());
        }
    }

    @Override
    public Set<OfflinePlayer> getOfflinePlayers(Settlement settlement) {
        return trusts.values().stream()
                .filter(t -> t.getSettlementId() == settlement.getId())
                .map(t -> Bukkit.getOfflinePlayer(t.getUniqueId()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<OfflinePlayer> getOfflinePlayers(long settlementId) {
        return trusts.values().stream()
                .filter(t -> t.getSettlementId() == settlementId)
                .map(t -> Bukkit.getOfflinePlayer(t.getUniqueId()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isTrusted(OfflinePlayer offlinePlayer, Settlement settlement) {
        return trusts.values().stream().anyMatch(t -> settlement.getId() == settlement.getId() && t.getUniqueId().equals(offlinePlayer.getUniqueId()));
    }

    @Override
    public boolean hasTrusts(OfflinePlayer offlinePlayer) {
        return trusts.values().stream().anyMatch(t -> t.getUniqueId().equals(offlinePlayer.getUniqueId()));
    }

    @Override
    public Set<Long> getSettlementIds(OfflinePlayer offlinePlayer) {
        return trusts.values().stream().filter(t -> t.getUniqueId().equals(offlinePlayer.getUniqueId()))
                .map(Trust::getSettlementId)
                .collect(Collectors.toSet());
    }
}