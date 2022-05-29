package com.huskydreaming.settlements.commands;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.inventories.InventorySupplier;
import com.huskydreaming.settlements.managers.SettlementManager;
import com.huskydreaming.settlements.persistence.Settlement;
import org.bukkit.Server;
import org.bukkit.command.Command;
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

public class CommandExecutor extends Command {

    private final Settlements settlements;
    protected final Set<CommandBase> subCommands;

    protected CommandExecutor(Settlements settlements, CommandBase... commandBases) {
        super("settlement");
        this.settlements = settlements;
        setAliases(Arrays.asList("s", "settlements"));
        this.subCommands = new HashSet<>(Arrays.asList(commandBases));
    }

    public static CommandExecutor create(Settlements settlements, CommandBase... commandBases) {
        return new CommandExecutor(settlements, commandBases);
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
                SettlementManager settlementManager = settlements.getSettlementManager();
                if (strings.length > 0) {
                    Optional<CommandBase> commandBase = subCommands.stream()
                            .filter(c -> c.name.equalsIgnoreCase(strings[0]))
                            .findFirst();

                    commandBase.ifPresent(c -> c.run(settlements, player, strings));
                } else {
                    Settlement settlement = settlementManager.getSettlement(player);
                    if(settlement != null) {
                        InventorySupplier.getSettlementInventory(settlement).open(player);
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
