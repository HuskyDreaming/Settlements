package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.implementations.RepositoryImpl;
import com.huskydreaming.huskycore.interfaces.Repository;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.SqlType;
import com.huskydreaming.settlements.database.dao.TrustDao;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.database.entities.Trust;
import com.huskydreaming.settlements.services.interfaces.TrustService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TrustServiceImpl implements TrustService {

    private final TrustDao trustDao;
    private final Repository<Trust> trustRepository;

    public TrustServiceImpl(SettlementPlugin plugin) {
        this.trustRepository = new RepositoryImpl<>();
        this.trustDao = new TrustDao(plugin);
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        trustDao.bulkImport(SqlType.TRUST, trustRepository::bulkAdd);
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        trustDao.bulkUpdate(SqlType.TRUST, trustRepository.values());
    }

    @Override
    public void clean(Settlement settlement) {
        Set<Long> keys = trustRepository.entries().stream()
                .filter(e -> e.getValue().getSettlementId() == settlement.getId())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        trustDao.bulkDelete(SqlType.TRUST, keys);
        trustRepository.keys().removeAll(keys);
    }

    @Override
    public void trust(OfflinePlayer offlinePlayer, Settlement settlement) {
        Trust trust = new Trust();
        trust.setUniqueId(offlinePlayer.getUniqueId());
        trust.setSettlementId(settlement.getId());

        trustDao.insert(trust).queue(i -> {
            trust.setId(i);
            trustRepository.add(trust);
        });
    }

    @Override
    public void unTrust(OfflinePlayer offlinePlayer, Settlement settlement) {
        unTrust(offlinePlayer, settlement.getRoleId());
    }

    @Override
    public void unTrust(OfflinePlayer offlinePlayer, long settlementId) {
        Predicate<Trust> trustPredicate = (t -> t.getSettlementId() == settlementId && t.getUniqueId().equals(offlinePlayer.getUniqueId()));
        trustRepository.values().stream().filter(trustPredicate).findFirst().ifPresent(trust -> trustDao.delete(trust).queue(success -> {
            if (success) trustRepository.remove(trust);
        }));

    }

    @Override
    public Set<OfflinePlayer> getOfflinePlayers(Settlement settlement) {
        return trustRepository.values().stream()
                .filter(t -> t.getSettlementId() == settlement.getId())
                .map(t -> Bukkit.getOfflinePlayer(t.getUniqueId()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<OfflinePlayer> getOfflinePlayers(long settlementId) {
        return trustRepository.values().stream()
                .filter(t -> t.getSettlementId() == settlementId)
                .map(t -> Bukkit.getOfflinePlayer(t.getUniqueId()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isTrusted(OfflinePlayer offlinePlayer, Settlement settlement) {
        return trustRepository.values().stream().anyMatch(t -> t.getSettlementId() == settlement.getId() && t.getUniqueId().equals(offlinePlayer.getUniqueId()));
    }

    @Override
    public boolean hasTrusts(OfflinePlayer offlinePlayer) {
        return trustRepository.values().stream().anyMatch(t -> t.getUniqueId().equals(offlinePlayer.getUniqueId()));
    }

    @Override
    public Set<Long> getSettlementIds(OfflinePlayer offlinePlayer) {
        return trustRepository.values().stream().filter(t -> t.getUniqueId().equals(offlinePlayer.getUniqueId()))
                .map(Trust::getSettlementId)
                .collect(Collectors.toSet());
    }
}