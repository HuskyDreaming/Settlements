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

import java.util.Map;

@Command(label = CommandLabel.SET_TAG, arguments = " [tag]")
public class SetTagCommand implements SubCommand {

    private final MemberService memberService;

    private final RoleService roleService;
    private final SettlementService settlementService;

    public SetTagCommand(HuskyPlugin plugin) {
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            if (!memberService.hasSettlement(player)) {
                player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
                return;
            }

            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            Role role = roleService.getRole(member);

            if (role.hasPermission(RolePermission.EDIT_TAGS) || settlement.isOwner(player)) {
                String string = strings[1];

                Map<String, Integer> defaults = settlementService.getDefaults();
                int minimumTagLength = defaults.getOrDefault("min-tag-length", 2);
                int maximumTagLength = defaults.getOrDefault("max-tag-length", 4);

                if (string.length() > maximumTagLength) {
                    player.sendMessage(Locale.SETTLEMENT_TAG_LONG.prefix(maximumTagLength));
                    return;
                }

                if (string.length() < minimumTagLength) {
                    player.sendMessage(Locale.SETTLEMENT_TAG_SHORT.prefix(minimumTagLength));
                    return;
                }

                settlement.setTag(string);
                player.sendMessage(Locale.SETTLEMENT_TAG.prefix(string));
            } else {
                player.sendMessage(Locale.NO_PERMISSIONS.prefix(RolePermission.LAND_CLAIM.getName()));
            }
        }
    }
}