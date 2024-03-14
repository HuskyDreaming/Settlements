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
                player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
                return;
            }

            Player target = Bukkit.getPlayer(strings[1]);
            if (target == null) {
                player.sendMessage(Locale.PLAYER_OFFLINE.prefix(strings[1]));
                return;
            }

            if (target == player) {
                player.sendMessage(Locale.INVITATION_SELF.prefix());
                return;
            }

            if (memberService.hasSettlement(target)) {
                player.sendMessage(Locale.SETTLEMENT_PLAYER_HAS_SETTLEMENT.prefix(target.getName()));
                return;
            }

            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            Role role = roleService.getRole(member);

            if (!(role.hasPermission(RolePermission.MEMBER_INVITE) || settlement.isOwner(player))) {
                player.sendMessage(Locale.NO_PERMISSIONS.prefix(RolePermission.MEMBER_INVITE));
                return;
            }

            invitationService.sendInvitation(target, member.getSettlement());
            player.sendMessage(Locale.INVITATION_SENT.prefix(target.getName()));
        }
    }
}