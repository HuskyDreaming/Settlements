package com.huskydreaming.settlements.services.base;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.services.implementations.*;
import com.huskydreaming.settlements.services.interfaces.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {

    public static Map<Class<?>, ServiceInterface> services = new HashMap<>();

    public static void Initialize() {
        services.put(MemberService.class, new MemberServiceImpl());
        services.put(ClaimService.class, new ClaimServiceImpl());
        services.put(DependencyService.class, new DependencyServiceImpl());
        services.put(InventoryService.class, new InventoryServiceImpl());
        services.put(InvitationService.class, new InvitationServiceImpl());
        services.put(RequestService.class, new RequestServiceImpl());
        services.put(RoleService.class, new RoleServiceImpl());
        services.put(SettlementService.class, new SettlementServiceImpl());
        services.put(YamlService.class, new YamlServiceImpl());
    }

    public static void Deserialize(SettlementPlugin settlementPlugin) {
        services.values().forEach(serviceInterface -> serviceInterface.deserialize(settlementPlugin));
    }

    public static void Serialize(SettlementPlugin settlementPlugin) {
        services.values().forEach(serviceInterface -> serviceInterface.serialize(settlementPlugin));
    }

    @NotNull
    public static <T> T Provide(Class<T> tClass) {
        //return tClass.isInstance(object) ? tClass.cast(object) : null;
        return tClass.cast(services.get(tClass));
    }
}
