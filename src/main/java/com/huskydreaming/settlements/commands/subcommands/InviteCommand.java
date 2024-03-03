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
import com.huskydreaming.settlements.services.interfaces.InvitationService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.INVITE, arguments = " [player]")
public class InviteCommand implements CommandInterface {

    private final MemberService memberService;
    private final InvitationService invitationService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public InviteCommand(SettlementPlugin plugin) {
        memberService = plugin.provide(MemberService.class);
        invitationService = plugin.provide(InvitationService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {

            if (!memberService.hasSettlement(player)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
                return;
            }

            Player target = Bukkit.getPlayer(strings[1]);
            if (target == null) {
                player.sendMessage(Remote.prefix(Locale.PLAYER_OFFLINE, strings[1]));
                return;
            }

            if (target == player) {
                player.sendMessage(Remote.prefix(Locale.INVITATION_SELF));
                return;
            }

            if (memberService.hasSettlement(target)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_HAS_SETTLEMENT, target.getName()));
                return;
            }

            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            Role role = roleService.getRole(member);

            if (!(role.hasPermission(RolePermission.MEMBER_INVITE) || settlement.isOwner(player))) {
                player.sendMessage(Remote.prefix(Locale.NO_PERMISSIONS, RolePermission.MEMBER_INVITE.getName()));
                return;
            }

            invitationService.sendInvitation(target, member.getSettlement());
            player.sendMessage(Remote.prefix(Locale.INVITATION_SENT, target.getName()));
        }
    }
}
