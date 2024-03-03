package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.RELOAD)
public class ReloadCommand implements CommandInterface {

    private final SettlementPlugin plugin;
    private final ClaimService claimService;
    private final LocaleService localeService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public ReloadCommand(SettlementPlugin plugin) {
        this.plugin = plugin;

        claimService = plugin.provide(ClaimService.class);
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

        claimService.serialize(plugin);
        memberService.serialize(plugin);
        roleService.serialize(plugin);
        settlementService.serialize(plugin);

        player.sendMessage(Remote.prefix(Locale.RELOAD));
    }
}
