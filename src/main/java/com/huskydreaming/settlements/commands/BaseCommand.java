package com.huskydreaming.settlements.commands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.abstraction.AbstractCommand;
import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.Parseable;
import com.huskydreaming.settlements.commands.subcommands.HelpCommand;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAnnotation(label = CommandLabel.SETTLEMENTS)
public class BaseCommand extends AbstractCommand {

    private final HuskyPlugin plugin;
    private final MemberService memberService;
    private final InventoryService inventoryService;

    public BaseCommand(HuskyPlugin plugin) {
        super(plugin);
        this.plugin = plugin;

        this.memberService = plugin.provide(MemberService.class);
        this.inventoryService = plugin.provide(InventoryService.class);
    }

    @Override
    public Parseable getPermission() {
        return Message.GENERAL_NO_PERMISSIONS;
    }

    @Override
    public Parseable getUsage() {
        return Message.GENERAL_UNKNOWN_SUBCOMMAND;
    }

    @Override
    public void onCommand(CommandSender commandSender, String[] strings) {
        if(commandSender instanceof  Player player) {
            if(memberService.hasSettlement(player)) {
                inventoryService.getMainInventory(plugin, player).open(player);
            } else {
                new HelpCommand(plugin).onCommand(player, strings);
            }
        }
    }
}