package com.huskydreaming.settlements.commands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.AbstractCommand;
import com.huskydreaming.huskycore.interfaces.Parseable;
import com.huskydreaming.settlements.commands.subcommands.HelpCommand;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.storage.Locale;
import org.bukkit.entity.Player;

public class BaseCommand extends AbstractCommand {

    private final HuskyPlugin plugin;
    private final MemberService memberService;
    private final InventoryService inventoryService;

    public BaseCommand(HuskyPlugin plugin) {
        super(CommandLabel.SETTLEMENTS, plugin);
        this.plugin = plugin;

        this.memberService = plugin.provide(MemberService.class);
        this.inventoryService = plugin.provide(InventoryService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        Member member = memberService.getCitizen(player);
        if (member != null) {
            inventoryService.getSettlementInventory(plugin, member.getSettlement()).open(player);
        } else {
            new HelpCommand(plugin).run(player, strings);
        }
    }

    @Override
    public Parseable getPermissionsLocale() {
        return Locale.NO_PERMISSIONS;
    }

    @Override
    public Parseable getUnknownSubCommandLocale() {
        return Locale.UNKNOWN_SUBCOMMAND;
    }
}