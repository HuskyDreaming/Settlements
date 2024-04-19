package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.CommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.types.Locale;
import org.bukkit.command.CommandSender;

@CommandAnnotation(label = CommandLabel.RELOAD)
public class ReloadCommand implements CommandProvider {

    private final HuskyPlugin plugin;
    private final ClaimService claimService;

    private final ConfigService configService;
    private final FlagService flagService;
    private final LocaleService localeService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public ReloadCommand(HuskyPlugin plugin) {
        this.plugin = plugin;

        claimService = plugin.provide(ClaimService.class);
        configService = plugin.provide(ConfigService.class);
        flagService = plugin.provide(FlagService.class);
        localeService = plugin.provide(LocaleService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(CommandSender sender, String[] strings) {
        localeService.getLocale().reload(plugin);
        localeService.getMenu().reload(plugin);

        claimService.serialize(plugin);
        configService.deserialize(plugin);
        flagService.serialize(plugin);
        memberService.serialize(plugin);
        roleService.serialize(plugin);
        settlementService.serialize(plugin);

        sender.sendMessage(Locale.RELOAD.prefix());
    }
}