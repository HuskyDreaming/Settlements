package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.Locale;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.RELOAD)
public class ReloadCommand implements SubCommand {

    private final HuskyPlugin plugin;
    private final ChunkService chunkService;
    private final FlagService flagService;
    private final LocaleService localeService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public ReloadCommand(HuskyPlugin plugin) {
        this.plugin = plugin;

        chunkService = plugin.provide(ChunkService.class);
        flagService = plugin.provide(FlagService.class);
        localeService = plugin.provide(LocaleService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        localeService.getLocale().reload(plugin);
        localeService.getMenu().reload(plugin);

        plugin.reloadConfig();
        plugin.saveDefaultConfig();
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();

        chunkService.serialize(plugin);
        flagService.serialize(plugin);
        memberService.serialize(plugin);
        roleService.serialize(plugin);
        settlementService.serialize(plugin);

        player.sendMessage(Locale.RELOAD.prefix());
    }
}