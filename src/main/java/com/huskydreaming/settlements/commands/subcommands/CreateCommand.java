package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
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

    public CreateCommand() {
        borderService = ServiceProvider.Provide(BorderService.class);
        claimService = ServiceProvider.Provide(ClaimService.class);
        dependencyService = ServiceProvider.Provide(DependencyService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        roleService = ServiceProvider.Provide(RoleService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            if(memberService.hasSettlement(player)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_EXISTS));
                return;
            }

            if(dependencyService.isWorldGuard(player)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_CREATE_WORLDGUARD));
                return;
            }

            String name = strings[1];
            Map<String, Integer> defaults = settlementService.getDefaults();
            int minimumNameLength = defaults.getOrDefault("min-name-length", 2);
            int maximumNameLength = defaults.getOrDefault("max-name-length", 10);

            if (settlementService.isSettlement(name)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_EXIST));
                return;
            }

            if(name.length() < minimumNameLength) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_CREATE_MIN_NAME_LENGTH, minimumNameLength));
                return;
            }

            if(name.length() > maximumNameLength) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_CREATE_MAX_NAME_LENGTH, maximumNameLength));
                return;
            }

            if(claimService.isDisabledWorld(player.getWorld())) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_CREATE_DISABLED_WORLD));
                return;
            }

            Chunk chunk = player.getLocation().getChunk();
            if (claimService.isClaim(chunk)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_ESTABLISHED));
                return;
            }

            Settlement settlement = settlementService.createSettlement(player, strings[1]);
            roleService.setup(settlement);
            memberService.add(player, settlement);
            claimService.setClaim(chunk, settlement);

            borderService.addPlayer(player, settlement.getName(), Color.AQUA);

            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_CREATED, strings[1]));
        }
    }
}
