package com.huskydreaming.settlements.services.base;

import com.google.inject.AbstractModule;
import com.huskydreaming.settlements.SettlementPlugin;

public class PluginModule extends AbstractModule {

    private final SettlementPlugin plugin;

    public PluginModule(SettlementPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        super.bind(SettlementPlugin.class).toInstance(plugin);
    }
}
