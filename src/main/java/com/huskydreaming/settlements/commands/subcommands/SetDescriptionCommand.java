package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;

@Command(label = CommandLabel.SETDESCRIPTION, arguments = " [description]")
public class SetDescriptionCommand implements CommandInterface {

    private final MemberService memberService;

    private final RoleService roleService;
    private final SettlementService settlementService;

    public SetDescriptionCommand(SettlementPlugin plugin) {
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length > 1) {
            if (!memberService.hasSettlement(player)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
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

                if(string.length() > maximumDescriptionLength) {
                    player.sendMessage(Remote.prefix(Locale.SETTLEMENT_DESCRIPTION_LONG, maximumDescriptionLength));
                    return;
                }

                if(string.length() < minimumDescriptionLength) {
                    player.sendMessage(Remote.prefix(Locale.SETTLEMENT_DESCRIPTION_SHORT, minimumDescriptionLength));
                    return;
                }

                settlement.setDescription(string);
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_DESCRIPTION, string));
            } else {
                player.sendMessage(Remote.prefix(Locale.NO_PERMISSIONS, RolePermission.LAND_CLAIM.getName()));
            }
        }
    }
}