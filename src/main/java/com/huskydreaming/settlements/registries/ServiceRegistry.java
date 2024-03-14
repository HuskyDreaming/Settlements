package com.huskydreaming.settlements.registries;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.services.implementations.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistry implements Registry {

    private final Map<Class<?>, ServiceInterface> services = new ConcurrentHashMap<>();

    @Override
    public void register(SettlementPlugin plugin) {
        services.put(LocaleService.class, new LocaleServiceImpl());
        services.put(ConfigService.class, new ConfigServiceImpl());
        services.put(ClaimService.class, new ClaimServiceImpl(plugin));
        services.put(BorderService.class, new BorderServiceImpl(plugin));
        services.put(DependencyService.class, new DependencyServiceImpl());
        services.put(InventoryService.class, new InventoryServiceImpl());
        services.put(InvitationService.class, new InvitationServiceImpl());
        services.put(MemberService.class, new MemberServiceImpl());
        services.put(RoleService.class, new RoleServiceImpl(plugin));
        services.put(SettlementService.class, new SettlementServiceImpl(plugin));
        services.values().forEach(serviceInterface -> serviceInterface.deserialize(plugin));
    }

    @Override
    public void unregister(SettlementPlugin plugin) {
        services.values().forEach(serviceInterface -> serviceInterface.serialize(plugin));
    }

    public Map<Class<?>, ServiceInterface> getServices() {
        return Collections.unmodifiableMap(services);
    }

    @NotNull
    public <T> T provide(Class<T> tClass) {
        return tClass.cast(services.get(tClass));
    }
}