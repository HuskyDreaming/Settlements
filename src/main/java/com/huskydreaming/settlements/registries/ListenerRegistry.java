package com.huskydreaming.settlements.registries;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.listeners.LandListener;
import com.huskydreaming.settlements.listeners.MemberListener;
import org.bukkit.plugin.PluginManager;

public class ListenerRegistry implements Registry {

    @Override
    public void register(SettlementPlugin plugin) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new LandListener(plugin), plugin);
        pluginManager.registerEvents(new MemberListener(plugin), plugin);
    }
}
