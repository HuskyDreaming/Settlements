package com.huskydreaming.settlements.commands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.InventoryService;
import com.huskydreaming.settlements.services.SettlementService;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandExecutor extends org.bukkit.command.Command {

    @Inject
    private InventoryService inventoryService;

    @Inject
    private SettlementService settlementService;

    protected final Set<CommandInterface> subCommands;

    protected CommandExecutor(CommandInterface... commandInterfaces) {
        super(CommandLabel.SETTLEMENTS.name().toLowerCase());
        this.subCommands = new HashSet<>(Arrays.asList(commandInterfaces));
    }

    public static CommandExecutor create(CommandInterface... commandInterfaces) {
        return new CommandExecutor(commandInterfaces);
    }

    public void register(Plugin plugin) {
        try {
            Server server = plugin.getServer();
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            CommandMap commandMap = (CommandMap) field.get(server);
            commandMap.register(getName(), this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player) {
            Player player = ((Player) commandSender).getPlayer();
            if(player != null) {
                if (strings.length > 0) {
                    Optional<CommandInterface> commandBase = subCommands.stream()
                            .filter(c -> {
                                String name = c.getLabel().name();
                                Set<String> aliases = Arrays.stream(c.getAliases())
                                        .map(String::toLowerCase)
                                        .collect(Collectors.toSet());

                                return name.equalsIgnoreCase(strings[0]) || aliases.contains(strings[0]);
                            }).findFirst();

                    commandBase.ifPresentOrElse(c -> c.run(player, strings), ()->
                            commandSender.sendMessage("Unknown Alias: Type /" + getName() + " for help.")
                    );
                } else {
                    Settlement settlement = settlementService.getSettlement(player);
                    if(settlement != null) {
                        inventoryService.getSettlementsInventory().open(player);
                    } else {
                        player.sendMessage("You do not seem to belong to a settlement.");
                    }
                }
            }
        } else {
            commandSender.sendMessage("You must be a player to execute this command.");
        }
        return false;
    }
}
