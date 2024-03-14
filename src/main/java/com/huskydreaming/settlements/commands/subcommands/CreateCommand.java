package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.Map;

@Command(label = CommandLabel.CREATE, arguments = " [name]")
public class CreateCommand implements CommandInterface {

    private final BorderService borderService;
    private final ClaimService claimService;
    private final DependencyService dependencyService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public CreateCommand(SettlementPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        claimService = plugin.provide(ClaimService.class);
        dependencyService = plugin.provide(DependencyService.class);
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
            Map<String, Integer> defaults = settlementService.getDefaults();
            int minimumNameLength = defaults.getOrDefault("min-name-length", 2);
            int maximumNameLength = defaults.getOrDefault("max-name-length", 10);

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

            if (claimService.isDisabledWorld(player.getWorld())) {
                player.sendMessage(Locale.SETTLEMENT_CREATE_DISABLED_WORLD.prefix());
                return;
            }

            Chunk chunk = player.getLocation().getChunk();
            if (claimService.isClaim(chunk)) {
                player.sendMessage(Locale.SETTLEMENT_ESTABLISHED.prefix());
                return;
            }

            Settlement settlement = settlementService.createSettlement(player, name);

            roleService.setup(name, settlement);
            claimService.setClaim(chunk, name);
            memberService.add(player, name, settlement.getDefaultRole());
            borderService.addPlayer(player, name, Color.AQUA);

            player.sendMessage(Locale.SETTLEMENT_CREATED.prefix(name));
        }
    }
}