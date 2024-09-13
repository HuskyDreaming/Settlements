package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.database.persistence.Config;
import com.huskydreaming.settlements.enumeration.types.SettlementDefaultType;
import com.huskydreaming.settlements.helpers.SettlementHelper;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.entity.Player;

@CommandAnnotation(label = CommandLabel.CREATE, arguments = " [name]")
public class CreateCommand implements PlayerCommandProvider {
    private final BorderService borderService;
    private final ClaimService claimService;
    private final ConfigService configService;
    private final DependencyService dependencyService;
    private final MemberService memberService;
    private final SettlementService settlementService;

    private final HuskyPlugin plugin;

    public CreateCommand(HuskyPlugin plugin) {
        this.plugin = plugin;

        borderService = plugin.provide(BorderService.class);
        claimService = plugin.provide(ClaimService.class);
        configService = plugin.provide(ConfigService.class);
        dependencyService = plugin.provide(DependencyService.class);
        memberService = plugin.provide(MemberService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 2) return;
        if (memberService.hasSettlement(player)) {
            player.sendMessage(Message.PLAYER_EXISTS.prefix());
            return;
        }

        if (configService.isDisabledWorld(player)) return;
        if (dependencyService.isTowny(player)) return;

        String name = strings[1].toLowerCase();
        Config config = configService.getConfig();
        int minimumNameLength = config.getSettlementDefault(SettlementDefaultType.MIN_NAME_LENGTH);
        int maximumNameLength = config.getSettlementDefault(SettlementDefaultType.MAX_NAME_LENGTH);

        if (settlementService.isSettlement(name)) {
            player.sendMessage(Message.CREATE_EXISTS.prefix());
            return;
        }

        if (name.length() < minimumNameLength) {
            player.sendMessage(Message.CREATE_MIN_NAME_LENGTH.prefix(minimumNameLength));
            return;
        }

        if (name.length() > maximumNameLength) {
            player.sendMessage(Message.CREATE_MAX_NAME_LENGTH.prefix(maximumNameLength));
            return;
        }

        World world = player.getWorld();
        if (config.containsDisableWorld(world) || world.getEnvironment() != World.Environment.NORMAL) {
            player.sendMessage(Message.CREATE_DISABLED_WORLD.prefix());
            return;
        }

        Chunk chunk = player.getLocation().getChunk();
        if (claimService.isClaim(chunk)) {
            player.sendMessage(Message.LAND_ESTABLISHED.prefix());
            return;
        }

        if (claimService.isAdjacentToExistingClaim(chunk)) {
            player.sendMessage(Message.LAND_ESTABLISHED_ADJACENT.prefix());
            return;
        }

        SettlementHelper.createSettlement(player, name, plugin).queue(i -> {
            player.sendMessage(Message.CREATE_SETTLEMENT.prefix(name));
            borderService.addPlayer(player, i, Color.AQUA);
        });
    }
}