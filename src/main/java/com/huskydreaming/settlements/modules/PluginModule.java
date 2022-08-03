package com.huskydreaming.settlements.modules;

import com.google.inject.AbstractModule;
import com.huskydreaming.settlements.SettlementPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginModule extends AbstractModule {

    private final SettlementPlugin plugin;

    public PluginModule(SettlementPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        super.bind(JavaPlugin.class).toInstance(plugin);
        super.bind(Plugin.class).toInstance(plugin);
        super.bind(SettlementPlugin.class).toInstance(plugin);
    }
}
