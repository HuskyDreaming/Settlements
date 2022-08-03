package com.huskydreaming.settlements.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.huskydreaming.settlements.commands.subcommands.*;
import com.huskydreaming.settlements.persistence.Citizen;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.CitizenService;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class CommandExecutor extends org.bukkit.command.Command {

    @Inject
    private CitizenService citizenService;

    @Inject
    private SettlementService settlementService;

    protected final Set<CommandInterface> subCommands;

    public CommandExecutor() {
        super(CommandLabel.SETTLEMENTS.name().toLowerCase());
        this.subCommands = new HashSet<>();
        setAliases(Arrays.asList("s", "settlement", "settle"));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player) {
            Player player = ((Player) commandSender).getPlayer();
            if(player != null) {
                if (strings.length > 0) {
                    Optional<CommandInterface> commandBase = subCommands.stream().filter(c -> {
                        String name = c.getLabel().name();
                        Set<String> aliases = Arrays.stream(c.getAliases())
                                .map(String::toLowerCase)
                                .collect(Collectors.toSet());

                        return name.equalsIgnoreCase(strings[0]) || aliases.contains(strings[0]);
                    }).findFirst();

                    commandBase.ifPresentOrElse(c -> {
                            if (c.requiresPermissions() && !player.hasPermission("settlements." + c.getLabel().name().toLowerCase())) {
                                player.sendMessage(Remote.parameterize(Locale.NO_PERMISSIONS, c.getLabel().name()));
                            }
                        }, () -> commandSender.sendMessage("Unknown Alias: Type /" + getName() + " for help.")
                    );
                } else {

                    Citizen citizen = citizenService.getCitizen(player);
                    if(citizen != null) {
                        Settlement settlement = settlementService.getSettlement(citizen.getSettlement());
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

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if(args.length == 1) {
            return subCommands.stream().filter(c -> !c.isDebug()).map(c -> c.getLabel().name().toLowerCase()).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public void setup(Plugin plugin, Injector injector) {
        Class<?>[] commandSubTypes = new Class<?>[]{
                AcceptCommand.class,
                ClaimCommand.class,
                CreateCommand.class,
                DenyCommand.class,
                DisbandCommand.class,
                InviteCommand.class,
                KickCommand.class,
                SetOwnerCommand.class,
                SetSpawnCommand.class,
                SpawnCommand.class,
                UnclaimCommand.class
        };

        try {
            for (Class<?> commandInterfaceClass : commandSubTypes) {
                Constructor<?> constructor = commandInterfaceClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                CommandInterface commandInterface = (CommandInterface) constructor.newInstance();
                subCommands.add(commandInterface);
                injector.injectMembers(commandInterface);
            }

            Server server = plugin.getServer();
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            CommandMap commandMap = (CommandMap) field.get(server);
            commandMap.register(getName(), this);
            injector.injectMembers(this);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
