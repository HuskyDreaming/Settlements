package com.huskydreaming.settlements;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.huskydreaming.settlements.commands.CommandExecutor;
import com.huskydreaming.settlements.commands.subcommands.*;
import com.huskydreaming.settlements.services.*;
import com.huskydreaming.settlements.services.base.PluginModule;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import com.huskydreaming.settlements.services.base.ServiceModule;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class SettlementPlugin extends JavaPlugin {

    private Injector injector;

    @Override
    public void onEnable() {

        injector = Guice.createInjector(new PluginModule(this), new ServiceModule());

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

        deserialize(
                CitizenService.class,
                ClaimService.class,
                DependencyService.class,
                InventoryService.class,
                InvitationService.class,
                RequestService.class,
                SettlementService.class,
                YamlService.class
        );
    }

    @Override
    public void onDisable() {
        serialize(
                CitizenService.class,
                ClaimService.class,
                DependencyService.class,
                InventoryService.class,
                InvitationService.class,
                RequestService.class,
                SettlementService.class,
                YamlService.class
        );
    }

    private void deserialize(Class<?>... classes) {
        Arrays.stream(classes).forEach(c -> ((ServiceInterface) injector.getInstance(c)).deserialize(this));
    }

    private void serialize(Class<?>... classes) {
        Arrays.stream(classes).forEach(c -> ((ServiceInterface) injector.getInstance(c)).serialize(this));
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();
        Arrays.asList(listeners).forEach(listener -> pluginManager.registerEvents(listener, this));
    }

    public Injector getInjector() {
        return injector;
    }
}
