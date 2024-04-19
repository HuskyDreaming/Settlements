package com.huskydreaming.settlements;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.settlements.commands.BaseCommand;
import com.huskydreaming.settlements.commands.subcommands.*;
import com.huskydreaming.settlements.listeners.FlagListener;
import com.huskydreaming.settlements.listeners.LandListener;
import com.huskydreaming.settlements.listeners.MemberListener;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.services.implementations.*;
import com.huskydreaming.settlements.services.interfaces.*;
import com.sk89q.worldguard.protection.flags.Flag;

public class SettlementPlugin extends HuskyPlugin {

    @Override
    public void onEnable() {
        super.onEnable();

        registerServices();
        registerCommands();
        registerListeners(new LandListener(this), new MemberListener(this), new FlagListener(this));
    }

    @Override
    public void onDisable() {
        serviceRegistry.serialize(this);
    }

    private void registerServices() {
        serviceRegistry.register(LocaleService.class, new LocaleServiceImpl(this));
        serviceRegistry.register(ConfigService.class, new ConfigServiceImpl(this));
        serviceRegistry.register(DependencyService.class, new DependencyServiceImpl());
        serviceRegistry.register(HomeService.class, new HomeServiceImpl());
        serviceRegistry.register(MemberService.class, new MemberServiceImpl());
        serviceRegistry.register(SettlementService.class, new SettlementServiceImpl(this));
        serviceRegistry.register(FlagService.class, new FlagServiceImpl(this));
        serviceRegistry.register(NotificationService.class, new NotificationServiceImpl(this));
        serviceRegistry.register(InvitationService.class, new InvitationServiceImpl());
        serviceRegistry.register(RoleService.class, new RoleServiceImpl(this));
        serviceRegistry.register(TrustService.class, new TrustServiceImpl());
        serviceRegistry.register(ClaimService.class, new ClaimServiceImpl());
        serviceRegistry.register(BorderService.class, new BorderServiceImpl(this));
        serviceRegistry.register(InventoryService.class, new InventoryServiceImpl(this));

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
        Config config = configService.getConfig();

        if(config.isTeleportation()) {
            commandRegistry.add(new SpawnCommand(this));
            commandRegistry.add(new SetSpawnCommand(this));
        }

        if(config.isTrusting()) {
            commandRegistry.add(new TrustCommand(this));
            commandRegistry.add(new UnTrustCommand(this));
        }

        if(config.isHomes()) {
            commandRegistry.add(new DeleteHomeCommand(this));
            commandRegistry.add(new HomeCommand(this));
            commandRegistry.add(new SetHomeCommand(this));
        }

        commandRegistry.add(new UnClaimCommand(this));
        commandRegistry.deserialize(this);
    }
}

/* TODO:

FIX: ACTIONBAR
FIX: DISBAND IN MENU [DONE]
FIX: Pressure plates/shulk sensors/tripwires
FIX: Minecarts
FIX: ENDER PORTALSA
FIX: PVP
FIX: FLAGS
FIX: TRUSTING MEMBERS MENU
FIX: ANIMAL KILLING
FIX: ENDER_PEARLING INTO CLAIM
FIX: LEADING ANIMALS
FIX: SETTLEMENT DETAULTS

CREATE: HOMES [IN-PROGRESS]

CREATE: SETROLE [DONE]

EDIT: VIEWING MENU [DONE]

 */