package com.huskydreaming.settlements.commands;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.InventoryService;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.services.base.ServiceRegistry;
import com.huskydreaming.settlements.services.base.ServiceType;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandExecutor extends org.bukkit.command.Command {

    protected final Set<CommandInterface> subCommands;

    protected CommandExecutor(Class<?>... commandInterfaces) {
        super(CommandLabel.SETTLEMENTS.name().toLowerCase());
        this.subCommands = new HashSet<>();

        for(Class<?> commandInterfaceClass : commandInterfaces) {
            try {
                Constructor<?> constructor = commandInterfaceClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                CommandInterface commandInterface = (CommandInterface) constructor.newInstance();

                subCommands.add(commandInterface);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static CommandExecutor create(Class<?>... commandInterfaces) {
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
                    SettlementService settlementService = (SettlementService) ServiceRegistry.getService(ServiceType.SETTLEMENT);

                    Settlement settlement = settlementService.getSettlement(player);
                    if(settlement != null) {
                        InventoryService inventoryService = (InventoryService) ServiceRegistry.getService(ServiceType.INVENTORY);
                        inventoryService.getSettlementInventory(settlement).open(player);
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
