package com.huskydreaming.settlements.services.base;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.services.implementations.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistry {

    private static final Map<ServiceType, ServiceInterface> services = new ConcurrentHashMap<>();

    public ServiceRegistry() {
        services.put(ServiceType.INVENTORY, new InventoryServiceImpl());
        services.put(ServiceType.INVITATION, new InvitationServiceImpl());
        services.put(ServiceType.REQUEST, new RequestServiceImpl());
        services.put(ServiceType.SETTLEMENT, new SettlementServiceImpl());
    }

    public void deserialize(SettlementPlugin settlementPlugin) {
        services.values().forEach(serviceInterface -> serviceInterface.deserialize(settlementPlugin));
    }

    public void serialize(SettlementPlugin settlementPlugin) {
        services.values().forEach(serviceInterface -> serviceInterface.serialize(settlementPlugin));
    }

    public static ServiceInterface getService(ServiceType type) {
        return services.get(type);
    }
}
