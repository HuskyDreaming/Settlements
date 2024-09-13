package com.huskydreaming.settlements;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.implementations.DatabaseMigrationServiceImpl;
import com.huskydreaming.huskycore.implementations.DatabaseServiceImpl;
import com.huskydreaming.huskycore.interfaces.database.base.DatabaseMigrationService;
import com.huskydreaming.huskycore.interfaces.database.base.DatabaseService;
import com.huskydreaming.settlements.commands.BaseCommand;
import com.huskydreaming.settlements.commands.subcommands.*;
import com.huskydreaming.settlements.database.migrations._1_InitialMigration;
import com.huskydreaming.settlements.listeners.FlagListener;
import com.huskydreaming.settlements.listeners.LandListener;
import com.huskydreaming.settlements.listeners.MemberListener;
import com.huskydreaming.settlements.services.implementations.*;
import com.huskydreaming.settlements.services.interfaces.*;

public class SettlementPlugin extends HuskyPlugin {

    @Override
    public void onEnable() {
        super.onEnable();

        registerServices();
        registerCommands();
        registerListeners(
                new LandListener(this),
                new MemberListener(this),
                new FlagListener(this)
        );
    }

    @Override
    public void onDisable() {
        serviceRegistry.serialize(this);
    }

    private void registerServices() {
        // Setup database
        DatabaseService databaseService = new DatabaseServiceImpl(this);
        DatabaseMigrationService databaseMigrationService = new DatabaseMigrationServiceImpl();
        databaseMigrationService.setConnector(databaseService.getDatabaseConnector());
        databaseMigrationService.loadMigrations(new _1_InitialMigration());

        serviceRegistry.register(ConfigService.class, new ConfigServiceImpl());
        serviceRegistry.register(LocaleService.class, new LocaleServiceImpl());
        serviceRegistry.register(DatabaseService.class, databaseService);
        serviceRegistry.register(DatabaseMigrationService.class, databaseMigrationService);
        serviceRegistry.register(DependencyService.class, new DependencyServiceImpl());
        serviceRegistry.register(SettlementService.class, new SettlementServiceImpl(this));
        serviceRegistry.register(ClaimService.class, new ClaimServiceImpl(this));
        serviceRegistry.register(ContainerService.class, new ContainerServiceImpl(this));
        serviceRegistry.register(FlagService.class, new FlagServiceImpl(this));
        serviceRegistry.register(HomeService.class, new HomeServiceImpl(this));
        serviceRegistry.register(RoleService.class, new RoleServiceImpl(this));
        serviceRegistry.register(MemberService.class, new MemberServiceImpl(this));
        serviceRegistry.register(PermissionService.class, new PermissionServiceImpl(this));
        serviceRegistry.register(NotificationService.class, new NotificationServiceImpl(this));
        serviceRegistry.register(InvitationService.class, new InvitationServiceImpl());
        serviceRegistry.register(TrustService.class, new TrustServiceImpl(this));
        serviceRegistry.register(BorderService.class, new BorderServiceImpl(this));
        serviceRegistry.register(InventoryService.class, new InventoryServiceImpl(this));
        serviceRegistry.deserialize(this);
    }

    private void registerCommands() {
        commandRegistry.setCommandExecutor(new BaseCommand(this));
        serviceRegistry.provide(ConfigService.class).setup(this);
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
        commandRegistry.add(new SetRoleCommand(this));
        commandRegistry.add(new SetTagCommand(this));
        commandRegistry.add(new UnClaimCommand(this));
        commandRegistry.deserialize(this);
    }
}