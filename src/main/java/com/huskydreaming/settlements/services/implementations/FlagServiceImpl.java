package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FlagServiceImpl implements FlagService {

    private final Map<Long, Flag> flags;
    private final FlagDao flagDao;

    public FlagServiceImpl(SettlementPlugin plugin) {
        this.flags = new ConcurrentHashMap<>();
        this.flagDao = new FlagDao(plugin);
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        flagDao.bulkImport(SqlType.FLAG, flags::putAll);
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        flagDao.bulkUpdate(SqlType.FLAG, flags.values());
    }

    @Override
    public void clean(Settlement settlement) {
        Set<Long> flagIds = flags.entrySet().stream()
                .filter(f -> f.getValue().getSettlementId() == settlement.getId())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        flagDao.bulkDelete(SqlType.FLAG, flagIds);
        flags.keySet().removeAll(flagIds);
    }

    @Override
    public void addFlag(Settlement settlement, FlagType flagType, Runnable runnable) {
        Flag flag = new Flag();
        flag.setType(flagType.toString());
        flag.setSettlementId(settlement.getId());

        flagDao.insert(flag).queue(i -> {
            flag.setId(i);
            flags.put(i, flag);
            runnable.run();
        });
    }

    @Override
    public void removeFlag(Settlement settlement, FlagType flagType, Runnable runnable) {
        flags.values().stream()
                .filter(f -> f.getSettlementId() == settlement.getId() && f.getType().equalsIgnoreCase(flagType.toString()))
                .findFirst().ifPresent(flag -> flagDao.delete(flag).queue(i -> {
                    flags.remove(flag.getId());
                    runnable.run();
                }));

    }

    @Override
    public boolean hasFlag(Settlement settlement, FlagType flagType) {
        return flags.values().stream().anyMatch(f ->
                f.getSettlementId() == settlement.getId() &&
                f.getType().equalsIgnoreCase(flagType.toString())
        );
    }

    @Override
    public boolean hasFlag(Claim claim, FlagType flagType) {
        return flags.values().stream().anyMatch(f ->
                f.getSettlementId() == claim.getSettlementId() &&
                        f.getType().equalsIgnoreCase(flagType.toString())
        );
    }
}