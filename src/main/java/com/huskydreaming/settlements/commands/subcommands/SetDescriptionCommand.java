package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.Locale;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;

@Command(label = CommandLabel.SET_DESCRIPTION, arguments = " [description]")
public class SetDescriptionCommand implements SubCommand {

    private final MemberService memberService;

    private final RoleService roleService;
    private final SettlementService settlementService;

    public SetDescriptionCommand(HuskyPlugin plugin) {
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length > 1) {
            if (!memberService.hasSettlement(player)) {
                player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
                return;
            }

            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            Role role = roleService.getRole(member);

            if (role.hasPermission(RolePermission.EDIT_DESCRIPTION) || settlement.isOwner(player)) {
                String[] array = Arrays.copyOfRange(strings, 1, strings.length);
                String string = String.join(" ", array);

                Map<String, Integer> defaults = settlementService.getDefaults();
                int minimumDescriptionLength = defaults.getOrDefault("min-description-length", 8);
                int maximumDescriptionLength = defaults.getOrDefault("max-description-length", 36);

                if (string.length() > maximumDescriptionLength) {
                    player.sendMessage(Locale.SETTLEMENT_DESCRIPTION_LONG.prefix(maximumDescriptionLength));
                    return;
                }

                if (string.length() < minimumDescriptionLength) {
                    player.sendMessage(Locale.SETTLEMENT_DESCRIPTION_SHORT.prefix(minimumDescriptionLength));
                    return;
                }

                settlement.setDescription(string);
                player.sendMessage(Locale.SETTLEMENT_DESCRIPTION.prefix(string));
            } else {
                player.sendMessage(Locale.NO_PERMISSIONS.prefix(RolePermission.LAND_CLAIM.getName()));
            }
        }
    }
}