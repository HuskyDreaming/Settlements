package com.huskydreaming.settlements;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.huskydreaming.settlements.commands.CommandExecutor;
import com.huskydreaming.settlements.listeners.LandListener;
import com.huskydreaming.settlements.listeners.RequestListener;
import com.huskydreaming.settlements.modules.PluginModule;
import com.huskydreaming.settlements.modules.ServiceModule;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class SettlementPlugin extends JavaPlugin {

    private Injector injector;

    @Override
    public void onEnable() {

        injector = Guice.createInjector(new PluginModule(this), new ServiceModule());
        new CommandExecutor().setup(this, injector);


        registerListeners(
                new LandListener(),
                new RequestListener()
        );
    }

    @Override
    public void onDisable() {
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();
        Arrays.asList(listeners).forEach(listener -> {
            pluginManager.registerEvents(listener, this);
            injector.injectMembers(listener);
        });
    }

}
