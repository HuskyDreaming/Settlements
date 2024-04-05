package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.enumeration.types.SettlementDefaultType;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.types.Locale;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.CREATE, arguments = " [name]")
public class CreateCommand implements SubCommand {

    private final BorderService borderService;
    private final ClaimService claimService;
    private final ConfigService configService;
    private final DependencyService dependencyService;
    private final FlagService flagService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public CreateCommand(HuskyPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        claimService = plugin.provide(ClaimService.class);
        configService = plugin.provide(ConfigService.class);
        dependencyService = plugin.provide(DependencyService.class);
        flagService = plugin.provide(FlagService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            if (memberService.hasSettlement(player)) {
                player.sendMessage(Locale.SETTLEMENT_PLAYER_EXISTS.prefix());
                return;
            }

            if (dependencyService.isTowny(player)) {

                player.sendMessage(Locale.SETTLEMENT_LAND_TOWNY.prefix());
                return;
            }
            if (dependencyService.isWorldGuard(player)) {
                player.sendMessage(Locale.SETTLEMENT_CREATE_WORLDGUARD.prefix());
                return;
            }

            String name = strings[1].toLowerCase();
            Config config = configService.getConfig();
            int minimumNameLength = config.getSettlementDefault(SettlementDefaultType.MIN_NAME_LENGTH);
            int maximumNameLength = config.getSettlementDefault(SettlementDefaultType.MAX_NAME_LENGTH);

            if (settlementService.isSettlement(name)) {
                player.sendMessage(Locale.SETTLEMENT_EXIST.prefix());
                return;
            }

            if (name.length() < minimumNameLength) {
                player.sendMessage(Locale.SETTLEMENT_CREATE_MIN_NAME_LENGTH.prefix(minimumNameLength));
                return;
            }

            if (name.length() > maximumNameLength) {
                player.sendMessage(Locale.SETTLEMENT_CREATE_MAX_NAME_LENGTH.prefix(maximumNameLength));
                return;
            }

            World world = player.getWorld();
            if (config.containsDisableWorld(world) || world.getEnvironment() != World.Environment.NORMAL) {
                player.sendMessage(Locale.SETTLEMENT_CREATE_DISABLED_WORLD.prefix());
                return;
            }

            Chunk chunk = player.getLocation().getChunk();
            if (claimService.isClaim(chunk)) {
                player.sendMessage(Locale.SETTLEMENT_ESTABLISHED.prefix());
                return;
            }

            if (claimService.isAdjacentToExistingClaim(chunk)) {
                player.sendMessage(Locale.SETTLEMENT_ESTABLISHED_ADJACENT.prefix());
                return;
            }

            Settlement settlement = settlementService.createSettlement(player, name);

            flagService.setup(name);
            roleService.setup(name, settlement);
            claimService.setClaim(chunk, name);
            memberService.add(player, name, settlement.getDefaultRole());
            borderService.addPlayer(player, name, Color.AQUA);

            player.sendMessage(Locale.SETTLEMENT_CREATED.prefix(name));
        }
    }
}