package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.settlements.database.SqlType;
import com.huskydreaming.settlements.database.dao.ContainerDao;
import com.huskydreaming.settlements.database.entities.Container;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.database.persistence.Config;
import com.huskydreaming.settlements.enumeration.types.SettlementDefaultType;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.ContainerService;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ContainerServiceImpl implements ContainerService {

    private final ContainerDao containerDao;
    private final Map<Long, Container> containers;
    private final ConfigService configService;

    public ContainerServiceImpl(HuskyPlugin plugin) {
        containerDao = new ContainerDao(plugin);
        containers = new ConcurrentHashMap<>();
        configService = plugin.provide(ConfigService.class);
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        containerDao.bulkUpdate(SqlType.CONTAINER, containers.values());
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        containerDao.bulkImport(SqlType.CONTAINER, containers::putAll);
    }

    @Override
    public void addContainer(Container container) {
        containers.put(container.getId(), container);
    }

    @Override
    public Container createDefaultContainer() {
        Config config = configService.getConfig();
        int maxClaims = config.getSettlementDefault(SettlementDefaultType.MAX_CLAIMS);
        int maxHomes = config.getSettlementDefault(SettlementDefaultType.MAX_HOMES);
        int maxMembers = config.getSettlementDefault(SettlementDefaultType.MAX_MEMBERS);
        int maxRoles = config.getSettlementDefault(SettlementDefaultType.MAX_ROLES);


        Container container = new Container();
        container.setMaxClaims(maxClaims);
        container.setMaxHomes(maxHomes);
        container.setMaxMembers(maxMembers);
        container.setMaxRoles(maxRoles);
        return container;
    }

    @Override
    public void deleteContainer(Container container) {
        containerDao.delete(container).queue(containers::remove);
    }

    @Override
    public Container getContainer(Settlement settlement) {
        return containers.values().stream()
                .filter(c -> c.getSettlementId() == settlement.getId())
                .findFirst()
                .orElse(null);
    }

    @Override
    public ContainerDao getDao() {
        return containerDao;
    }

    @Override
    public void clean(Settlement settlement) {
        Set<Long> containerIds = containers.entrySet().stream()
                .filter(e -> e.getValue().getSettlementId() == settlement.getId())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());


        containerDao.bulkDelete(SqlType.CLAIM, containerIds);
        containers.keySet().removeAll(containerIds);
    }
}
