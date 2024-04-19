package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.HomeService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.types.Locale;
import org.bukkit.entity.Player;

@CommandAnnotation(label = CommandLabel.SET_HOME)
public class SetHomeCommand implements PlayerCommandProvider {

    private final HomeService homeService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public SetHomeCommand(HuskyPlugin plugin) {
        homeService = plugin.provide(HomeService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 2) return;
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Role role = roleService.getRole(member);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());

        if (!(role.hasPermission(RolePermission.EDIT_HOMES) || settlement.isOwner(player))) {
            player.sendMessage(Locale.NO_PERMISSIONS.prefix());
            return;
        }

        homeService.setHome(member.getSettlement(), strings[1], player);
        player.sendMessage(Locale.HOME_SET.prefix(strings[1]));
    }
}