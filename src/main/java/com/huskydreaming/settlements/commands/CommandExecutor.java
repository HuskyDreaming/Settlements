package com.huskydreaming.settlements.commands;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.subcommands.HelpCommand;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.registries.CommandRegistry;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class CommandExecutor extends org.bukkit.command.Command {

    private final SettlementPlugin plugin;
    private final CommandRegistry commandRegistry;
    private final InventoryService inventoryService;
    private final MemberService memberService;

    public CommandExecutor(SettlementPlugin plugin) {
        super(CommandLabel.SETTLEMENTS.name().toLowerCase());
        this.plugin = plugin;

        setAliases(Arrays.asList("s", "settlement", "settle"));

        commandRegistry = plugin.getCommandRegistry();
        inventoryService = plugin.provide(InventoryService.class);
        memberService = plugin.provide(MemberService.class);
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length > 0) {
                Optional<CommandInterface> commandBase = commandRegistry.getCommands().stream().filter(c -> {
                    String name = c.getLabel().name();
                    Set<String> aliases = Arrays.stream(c.getAliases())
                            .map(String::toLowerCase)
                            .collect(Collectors.toSet());

                    return name.equalsIgnoreCase(strings[0]) || aliases.contains(strings[0]);
                }).findFirst();

                commandBase.ifPresentOrElse(c -> {
                            if (!player.hasPermission("settlements." + c.getLabel().name().toLowerCase())) {
                                player.sendMessage(Locale.NO_PERMISSIONS.prefix("/settlements " + c.getLabel().name() + c.getArguments()));
                            } else {
                                c.run(player, strings);
                            }
                        }, () -> commandSender.sendMessage("Unknown Alias: Type /" + getName() + " for help.")
                );
            } else {
                Member member = memberService.getCitizen(player);
                if (member != null) {
                    inventoryService.getSettlementInventory(plugin, member.getSettlement()).open(player);
                } else {
                    new HelpCommand(plugin).run(player, strings);
                }
            }
        } else {
            commandSender.sendMessage("You must be a player to execute this command.");
        }
        return false;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return commandRegistry.getCommands().stream().filter(c -> !c.isDebug()).map(c -> c.getLabel().name().toLowerCase()).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}