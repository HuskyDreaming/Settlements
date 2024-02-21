package com.huskydreaming.settlements.commands;

import com.huskydreaming.settlements.commands.subcommands.HelpCommand;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class CommandExecutor extends org.bukkit.command.Command {

    private final MemberService memberService;

    private final SettlementService settlementService;

    private final Set<CommandInterface> subCommands;

    public CommandExecutor() {
        super(CommandLabel.SETTLEMENTS.name().toLowerCase());
        this.subCommands = new HashSet<>();
        setAliases(Arrays.asList("s", "settlement", "settle"));

        memberService = ServiceProvider.Provide(MemberService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
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
                            } else {
                                c.run(player, strings);
                            }
                        }, () -> commandSender.sendMessage("Unknown Alias: Type /" + getName() + " for help.")
                    );
                } else {

                    Member member = memberService.getCitizen(player);
                    if(member != null) {
                        Settlement settlement = settlementService.getSettlement(member.getSettlement());
                        ServiceProvider.Provide(InventoryService.class).getSettlementInventory(settlement).open(player);
                    } else {
                        new HelpCommand(this).run(player, strings);
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

    public void setup(CommandInterface... commandInterfaces) {
        try {
            subCommands.addAll(Arrays.asList(commandInterfaces));

            Server server = Bukkit.getServer();
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            CommandMap commandMap = (CommandMap) field.get(server);
            commandMap.register(getName(), this);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<CommandInterface> getSubCommands() {
        return Collections.unmodifiableSet(subCommands);
    }
}
