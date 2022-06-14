package com.huskydreaming.settlements.modules;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.services.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class PluginModule extends AbstractModule {

    protected final Plugin plugin;

    public PluginModule(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        super.bind(Plugin.class).toInstance(plugin);
        super.bind(PluginManager.class).toInstance(plugin.getServer().getPluginManager());

        bind(InventoryService.class).to(InventoryServiceImpl.class);
        bind(InvitationService.class).to(InvitationServiceImpl.class);
        bind(RequestService.class).to(RequestServiceImpl.class);
        bind(SettlementService.class).to(SettlementServiceImpl.class);
        bind(VisualisationService.class).to(VisualiserServiceImpl.class);
    }


    protected void configureListeners(Multibinder<Listener> binder) {

    }

    protected void configureCommands(Multibinder<CommandInterface> binder) {

    }
}
