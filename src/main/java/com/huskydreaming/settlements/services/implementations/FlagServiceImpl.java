package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.implementations.RepositoryImpl;
import com.huskydreaming.huskycore.interfaces.Repository;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.SqlType;
import com.huskydreaming.settlements.database.dao.FlagDao;
import com.huskydreaming.settlements.database.entities.Claim;
import com.huskydreaming.settlements.database.entities.Flag;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.enumeration.FlagType;
import com.huskydreaming.settlements.services.interfaces.FlagService;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FlagServiceImpl implements FlagService {

    private final Repository<Flag> flagRepository;
    private final FlagDao flagDao;

    public FlagServiceImpl(SettlementPlugin plugin) {
        this.flagRepository = new RepositoryImpl<>();
        this.flagDao = new FlagDao(plugin);
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        flagDao.bulkImport(SqlType.FLAG, flagRepository::bulkAdd);
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        flagDao.bulkUpdate(SqlType.FLAG, flagRepository.values());
    }

    @Override
    public void clean(Settlement settlement) {
        Set<Long> flagIds = flagRepository.entries().stream()
                .filter(f -> f.getValue().getSettlementId() == settlement.getId())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        flagDao.bulkDelete(SqlType.FLAG, flagIds);
        flagRepository.keys().removeAll(flagIds);
    }

    @Override
    public void addFlag(Settlement settlement, FlagType flagType, Runnable runnable) {
        Flag flag = new Flag();
        flag.setType(flagType.toString());
        flag.setSettlementId(settlement.getId());

        flagDao.insert(flag).queue(i -> {
            flag.setId(i);
            flagRepository.add(flag);
            runnable.run();
        });
    }

    @Override
    public void removeFlag(Settlement settlement, FlagType flagType, Runnable runnable) {
        flagRepository.values().stream()
                .filter(f -> f.getSettlementId() == settlement.getId() && f.getType().equalsIgnoreCase(flagType.toString()))
                .findFirst().ifPresent(flag -> flagDao.delete(flag).queue(i -> {
                    flagRepository.remove(flag);
                    runnable.run();
                }));

    }

    @Override
    public boolean hasFlag(Settlement settlement, FlagType flagType) {
        return flagRepository.values().stream().anyMatch(f ->
                f.getSettlementId() == settlement.getId() &&
                f.getType().equalsIgnoreCase(flagType.toString())
        );
    }

    @Override
    public boolean hasFlag(Claim claim, FlagType flagType) {
        return flagRepository.values().stream().anyMatch(f ->
                f.getSettlementId() == claim.getSettlementId() &&
                        f.getType().equalsIgnoreCase(flagType.toString())
        );
    }
}