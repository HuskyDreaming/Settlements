package com.huskydreaming.settlements;

import com.huskydreaming.settlements.registries.CommandRegistry;
import com.huskydreaming.settlements.registries.ListenerRegistry;
import com.huskydreaming.settlements.registries.ServiceRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SettlementPlugin extends JavaPlugin {

    private CommandRegistry commandRegistry;
    private ServiceRegistry serviceRegistry;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        getLogger().info("Loaded config.yml");
        saveConfig();

        serviceRegistry = new ServiceRegistry();
        serviceRegistry.register(this);

        commandRegistry = new CommandRegistry();
        commandRegistry.register(this);

        ListenerRegistry listenerRegistry = new ListenerRegistry();
        listenerRegistry.register(this);
    }

    @Override
    public void onDisable() {
        serviceRegistry.unregister(this);
    }

    @NotNull
    public <T> T provide(Class<T> tClass) {
        return tClass.cast(serviceRegistry.getServices().get(tClass));
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }
}