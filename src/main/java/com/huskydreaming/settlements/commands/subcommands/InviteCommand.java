package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.database.entities.*;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CommandAnnotation(label = CommandLabel.INVITE, arguments = " [Player]")
public class InviteCommand implements PlayerCommandProvider {

    private final ContainerService containerService;
    private final MemberService memberService;
    private final InvitationService invitationService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public InviteCommand(HuskyPlugin plugin) {
        containerService = plugin.provide(ContainerService.class);
        memberService = plugin.provide(MemberService.class);
        invitationService = plugin.provide(InvitationService.class);
        permissionService = plugin.provide(PermissionService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 2) return;

        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Message.PLAYER_NULL.prefix());
            return;
        }

        Player target = Bukkit.getPlayer(strings[1]);
        if (target == null) {
            player.sendMessage(Message.PLAYER_OFFLINE.prefix(strings[1]));
            return;
        }

        if (target == player) {
            player.sendMessage(Message.INVITATION_SELF.prefix());
            return;
        }

        if (memberService.hasSettlement(target)) {
            player.sendMessage(Message.PLAYER_HAS_SETTLEMENT.prefix(target.getName()));
            return;
        }

        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);
        Role role = roleService.getRole(member);

        Set<PermissionType> permissions = permissionService.getPermissions(role);
        if(!(permissions.contains(PermissionType.MEMBER_INVITE) || settlement.isOwner(player))) {
            player.sendMessage(Message.GENERAL_NO_PERMISSIONS.prefix());
            return;
        }

        Set<Member> members = memberService.getMembers(settlement);
        Container container = containerService.getContainer(settlement);
        int maxMembers = container.getMaxMembers();
        if (members.size() >= maxMembers) {
            player.sendMessage(Message.MEMBER_MAX.prefix(maxMembers));
            return;
        }

        target.sendMessage(Message.INVITATION_RECEIVED.prefix(member.getSettlementId()));
        invitationService.sendInvitation(target, settlement);
        player.sendMessage(Message.INVITATION_SENT.prefix(target.getName()));
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (strings.length == 2) return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        return List.of();
    }
}