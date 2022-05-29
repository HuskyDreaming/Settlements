package com.huskydreaming.settlements;

import com.huskydreaming.settlements.commands.CommandExecutor;
import com.huskydreaming.settlements.commands.subcommands.*;
import com.huskydreaming.settlements.listeners.LandListener;
import com.huskydreaming.settlements.listeners.RequestListener;
import com.huskydreaming.settlements.managers.InvitationManager;
import com.huskydreaming.settlements.managers.RequestManager;
import com.huskydreaming.settlements.managers.SettlementManager;
import com.huskydreaming.settlements.managers.VisualisationManager;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Settlements extends JavaPlugin {

    private static Settlements INSTANCE;

    private InventoryManager inventoryManager;
    private SettlementManager settlementManager;
    private RequestManager requestManager;
    private InvitationManager invitationManager;
    private VisualisationManager visualisationManager;

    @Override
    public void onEnable() {
        INSTANCE = this;

        settlementManager = new SettlementManager();
        settlementManager.deserialize(this);

        visualisationManager = new VisualisationManager();
        visualisationManager.run(this);

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        requestManager = new RequestManager();
        invitationManager = new InvitationManager();

        CommandExecutor.create(this,
                new AcceptCommand(),
                new ClaimCommand(),
                new CreateCommand(),
                new DisbandCommand(),
                new DenyCommand(),
                new InviteCommand(),
                new ListCommand(),
                new SetOwnerCommand(),
                new SetSpawnCommand(),
                new ShowCommand(),
                new SpawnCommand(),
                new UnclaimCommand()
        ).register(this);

        registerListeners(
                new LandListener(this),
                new RequestListener(this)
        );
    }

    @Override
    public void onDisable() {
        settlementManager.serialize(this);
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();
        for(Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

    public VisualisationManager getVisualisationManager() {
        return visualisationManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public InvitationManager getInvitationManager() {
        return invitationManager;
    }

    public SettlementManager getSettlementManager() {
        return settlementManager;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public static Settlements getInstance() {
        return INSTANCE;
    }
}
