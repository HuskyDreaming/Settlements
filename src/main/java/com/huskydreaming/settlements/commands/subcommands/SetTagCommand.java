package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.entity.Player;

import java.util.Map;

@Command(label = CommandLabel.SETTAG, arguments = " [tag]")
public class SetTagCommand implements CommandInterface {

    private final MemberService memberService;

    private final RoleService roleService;
    private final SettlementService settlementService;

    public SetTagCommand() {
        memberService = ServiceProvider.Provide(MemberService.class);
        roleService = ServiceProvider.Provide(RoleService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            if (!memberService.hasSettlement(player)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
                return;
            }

            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            Role role = roleService.getRole(settlement, member);

            if (role.hasPermission(RolePermission.EDIT_TAGS) || settlement.isOwner(player)) {
                String string = strings[1];

                Map<String, Integer> defaults = settlementService.getDefaults();
                int minimumTagLength = defaults.getOrDefault("min-tag-length", 2);
                int maximumTagLength = defaults.getOrDefault("max-tag-length", 4);

                if(string.length() > maximumTagLength) {
                    player.sendMessage(Remote.prefix(Locale.SETTLEMENT_TAG_LONG, maximumTagLength));
                    return;
                }

                if(string.length() < minimumTagLength) {
                    player.sendMessage(Remote.prefix(Locale.SETTLEMENT_TAG_SHORT, minimumTagLength));
                    return;
                }

                settlement.setTag(string);
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_TAG, string));
            } else {
                player.sendMessage(Remote.prefix(Locale.NO_PERMISSIONS, RolePermission.LAND_CLAIM.getName()));
            }
        }
    }
}