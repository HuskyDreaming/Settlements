package com.huskydreaming.settlements;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.huskydreaming.settlements.commands.CommandExecutor;
import com.huskydreaming.settlements.commands.subcommands.*;
import com.huskydreaming.settlements.services.*;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import com.huskydreaming.settlements.services.base.ServiceModule;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class SettlementPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        Injector injector = Guice.createInjector(new ServiceModule());

        CommandExecutor.inject(injector,
                AcceptCommand.class,
                ClaimCommand.class,
                CreateCommand.class,
                DenyCommand.class,
                DisbandCommand.class,
                InviteCommand.class,
                KickCommand.class,
                SetOwnerCommand.class,
                SetSpawnCommand.class,
                SpawnCommand.class,
                UnclaimCommand.class
        ).register(this);

        deserialize(injector,
                CitizenService.class,
                ClaimService.class,
                SettlementService.class,
                InventoryService.class,
                InvitationService.class
        );
    }

    @Override
    public void onDisable() {

    }

    private void deserialize(Injector injector, Class<?>... classes) {
        Arrays.stream(classes).forEach(c -> ((ServiceInterface) injector.getInstance(c)).deserialize(this));
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();
        Arrays.asList(listeners).forEach(listener -> pluginManager.registerEvents(listener, this));
    }
}
