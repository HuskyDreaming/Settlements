package com.huskydreaming.settlements;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.settlements.commands.BaseCommand;
import com.huskydreaming.settlements.commands.subcommands.*;
import com.huskydreaming.settlements.listeners.LandListener;
import com.huskydreaming.settlements.listeners.MemberListener;
import com.huskydreaming.settlements.services.implementations.*;
import com.huskydreaming.settlements.services.interfaces.*;

public class SettlementPlugin extends HuskyPlugin {

    @Override
    public void onEnable() {
        super.onEnable();

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        getLogger().info("Loaded config.yml");
        saveConfig();

        registerServices();
        registerCommands();
        registerListeners(
                new LandListener(this),
                new MemberListener(this)
        );
    }

    @Override
    public void onDisable() {
        serviceRegistry.serialize(this);
    }

    private void registerServices() {
        serviceRegistry.register(ConfigService.class, new ConfigServiceImpl());
        serviceRegistry.register(ChunkService.class, new ChunkServiceImpl(this));
        serviceRegistry.register(BorderService.class, new BorderServiceImpl(this));
        serviceRegistry.register(DependencyService.class, new DependencyServiceImpl());
        serviceRegistry.register(InventoryService.class, new InventoryServiceImpl());
        serviceRegistry.register(InvitationService.class, new InvitationServiceImpl());
        serviceRegistry.register(LocaleService.class, new LocaleServiceImpl());
        serviceRegistry.register(MemberService.class, new MemberServiceImpl());
        serviceRegistry.register(FlagService.class, new FlagServiceImpl(this));
        serviceRegistry.register(RoleService.class, new RoleServiceImpl(this));
        serviceRegistry.register(SettlementService.class, new SettlementServiceImpl(this));
        serviceRegistry.deserialize(this);
    }

    private void registerCommands() {
        commandRegistry.setCommandExecutor(new BaseCommand(this));
        commandRegistry.add(new AcceptCommand(this));
        commandRegistry.add(new AdminCommand(this));
        commandRegistry.add(new AutoClaimCommand(this));
        commandRegistry.add(new ClaimCommand(this));
        commandRegistry.add(new CreateCommand(this));
        commandRegistry.add(new CreateRoleCommand(this));
        commandRegistry.add(new DeleteRoleCommand(this));
        commandRegistry.add(new DenyCommand(this));
        commandRegistry.add(new DisbandCommand(this));
        commandRegistry.add(new HelpCommand(this));
        commandRegistry.add(new InviteCommand(this));
        commandRegistry.add(new KickCommand(this));
        commandRegistry.add(new LeaveCommand(this));
        commandRegistry.add(new ListCommand(this));
        commandRegistry.add(new ReloadCommand(this));
        commandRegistry.add(new SetDescriptionCommand(this));
        commandRegistry.add(new SetOwnerCommand(this));
        commandRegistry.add(new SetTagCommand(this));

        ConfigService configService = serviceRegistry.provide(ConfigService.class);

        if(configService.deserializeTeleportation(this)) {
            commandRegistry.add(new SpawnCommand(this));
            commandRegistry.add(new SetSpawnCommand(this));
        }

        commandRegistry.add(new UnClaimCommand(this));
        commandRegistry.deserialize(this);
    }
}