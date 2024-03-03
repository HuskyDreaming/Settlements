package com.huskydreaming.settlements.registries;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.CommandExecutor;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.subcommands.*;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CommandRegistry implements Registry {

    private final Set<CommandInterface> commands = new HashSet<>();

    public Set<CommandInterface> getCommands() {
        return Collections.unmodifiableSet(commands);
    }

    @Override
    public void register(SettlementPlugin plugin) {
        CommandExecutor commandExecutor = new CommandExecutor(plugin);
        commands.add(new AcceptCommand(plugin));
        commands.add(new AdminCommand(plugin));
        commands.add(new AutoClaimCommand(plugin));
        commands.add(new ClaimCommand(plugin));
        commands.add(new CreateCommand(plugin));
        commands.add(new CreateRoleCommand(plugin));
        commands.add(new DeleteRoleCommand(plugin));
        commands.add(new DenyCommand(plugin));
        commands.add(new DisbandCommand(plugin));
        commands.add(new InviteCommand(plugin));
        commands.add(new HelpCommand(plugin));
        commands.add(new KickCommand(plugin));
        commands.add(new LeaveCommand(plugin));
        commands.add(new ListCommand(plugin));
        commands.add(new ReloadCommand(plugin));
        commands.add(new SetDescriptionCommand(plugin));
        commands.add(new SetOwnerCommand(plugin));
        commands.add(new SetSpawnCommand(plugin));
        commands.add(new SetTagCommand(plugin));
        commands.add(new SpawnCommand(plugin));
        commands.add(new UnclaimCommand(plugin));

        try {
            Server server = Bukkit.getServer();
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            CommandMap commandMap = (CommandMap) field.get(server);
            commandMap.register("settlement", commandExecutor);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
