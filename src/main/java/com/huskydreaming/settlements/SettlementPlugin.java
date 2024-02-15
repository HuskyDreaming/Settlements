package com.huskydreaming.settlements;

import com.huskydreaming.settlements.commands.CommandExecutor;
import com.huskydreaming.settlements.commands.subcommands.*;
import com.huskydreaming.settlements.listeners.LandListener;
import com.huskydreaming.settlements.listeners.MemberListener;
import com.huskydreaming.settlements.listeners.RequestListener;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.BorderService;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class SettlementPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        ServiceProvider.Initialize();
        ServiceProvider.Deserialize(this);

        new CommandExecutor().setup(
                new AcceptCommand(),
                new ClaimCommand(),
                new CreateCommand(),
                new DenyCommand(),
                new DisbandCommand(),
                new InviteCommand(),
                new KickCommand(),
                new SetDescriptionCommand(),
                new SetOwnerCommand(),
                new SetSpawnCommand(),
                new SpawnCommand(),
                new UnclaimCommand()
        );

        registerListeners(
                new LandListener(),
                new MemberListener(),
                new RequestListener()
        );

        ServiceProvider.Provide(BorderService.class).run(this);
    }

    @Override
    public void onDisable() {
        ServiceProvider.Serialize(this);
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();
        Arrays.asList(listeners).forEach(listener -> pluginManager.registerEvents(listener, this));
    }
}
