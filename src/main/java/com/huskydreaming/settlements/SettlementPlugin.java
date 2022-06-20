package com.huskydreaming.settlements;

import com.huskydreaming.settlements.commands.CommandExecutor;
import com.huskydreaming.settlements.commands.subcommands.*;
import com.huskydreaming.settlements.listeners.LandListener;
import com.huskydreaming.settlements.listeners.RequestListener;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.services.base.ServiceRegistry;
import com.huskydreaming.settlements.services.base.ServiceType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class SettlementPlugin extends JavaPlugin {

    private ServiceRegistry serviceRegistry;

    @Override
    public void onEnable() {
        serviceRegistry = new ServiceRegistry();
        serviceRegistry.deserialize(this);

        CommandExecutor.create(
                AcceptCommand.class,
                ClaimCommand.class,
                CreateCommand.class,
                DenyCommand.class,
                DisbandCommand.class,
                InviteCommand.class,
                KickCommand.class,
                ListCommand.class,
                SetOwnerCommand.class,
                SetSpawnCommand.class,
                SpawnCommand.class,
                UnclaimCommand.class
        ).register(this);

        SettlementService settlementService = (SettlementService) ServiceRegistry.getService(ServiceType.SETTLEMENT);
        registerListeners(new LandListener(settlementService), new RequestListener(this));
    }

    @Override
    public void onDisable() {
        serviceRegistry.serialize(this);
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();
        Arrays.asList(listeners).forEach(listener -> pluginManager.registerEvents(listener, this));
    }
}
