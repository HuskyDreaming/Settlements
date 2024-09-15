package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.implementations.RepositoryImpl;
import com.huskydreaming.huskycore.interfaces.Repository;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.SqlType;
import com.huskydreaming.settlements.database.dao.SettlementDao;
import com.huskydreaming.settlements.database.entities.Claim;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class SettlementServiceImpl implements SettlementService {

    private final SettlementDao settlementDao;
    private final Repository<Settlement> settlementRepository;

    public SettlementServiceImpl(SettlementPlugin plugin) {
        this.settlementRepository = new RepositoryImpl<>();
        this.settlementDao = new SettlementDao(plugin);
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        settlementDao.bulkUpdate(SqlType.SETTLEMENT, settlementRepository.values());
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        settlementDao.bulkImport(SqlType.SETTLEMENT, settlementRepository::bulkAdd);
    }

    @Override
    public Settlement createSettlement(Player player, String name) {
        Location location = player.getLocation();
        World world = location.getWorld();
        if(world == null) return null;
        Settlement settlement = new Settlement();
        settlement.setName(name);
        settlement.setDescription("A Peaceful Settlement");
        settlement.setOwnerUUID(player.getUniqueId());
        settlement.setWorldUID(world.getUID());
        settlement.setX(location.getX());
        settlement.setY(location.getY());
        settlement.setZ(location.getZ());
        settlement.setYaw(location.getYaw());
        settlement.setPitch(location.getPitch());
        return settlement;
    }

    @Override
    public void addSettlement(Settlement settlement) {
        settlementRepository.add(settlement);
    }

    @Override
    public void disbandSettlement(Settlement settlement) {
        settlementDao.delete(settlement).queue(success -> {
            if(success)settlementRepository.remove(settlement);
        });
    }

    @Override
    public boolean isSettlement(String name) {
        return settlementRepository.values().stream().anyMatch(s -> s.getName().equalsIgnoreCase(name));
    }

    @Override
    public Settlement getSettlement(String name) {
        return settlementRepository.values().stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Settlement getSettlement(Claim claim) {
        return settlementRepository.get(claim.getSettlementId());
    }

    @Override
    public Settlement getSettlement(Member member) {
        return settlementRepository.get(member.getSettlementId());
    }

    @Override
    public Settlement getSettlement(long id) {
        return settlementRepository.get(id);
    }

    @Override
    public Map<Long, Settlement> getSettlements() {
        return settlementRepository.all();
    }

    @Override
    public Set<Settlement> getSettlements(Set<Long> settlementIds) {
        return settlementRepository.values().stream()
                .filter(settlement -> settlementIds.contains(settlement.getId()))
                .collect(Collectors.toSet());
    }

    @Override
    public SettlementDao getDao() {
        return settlementDao;
    }
}