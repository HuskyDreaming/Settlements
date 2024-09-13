package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.database.entities.*;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

@CommandAnnotation(label = CommandLabel.ACCEPT, arguments = " [settlement]")
public class AcceptCommand implements PlayerCommandProvider {

    private final BorderService borderService;
    private final ClaimService claimService;
    private final MemberService memberService;
    private final InvitationService invitationService;
    private final RoleService roleService;
    private final SettlementService settlementService;
    private final TrustService trustService;

    public AcceptCommand(HuskyPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        claimService = plugin.provide(ClaimService.class);
        memberService = plugin.provide(MemberService.class);
        invitationService = plugin.provide(InvitationService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
        trustService = plugin.provide(TrustService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 2) return;
        String string = strings[1];
        Settlement settlement = settlementService.getSettlement(string);
        if (settlement == null) {
            player.sendMessage(Message.NULL.prefix(string));
            return;
        }

        if (invitationService.hasNoInvitation(player, settlement)) {
            player.sendMessage(Message.INVITATION_NULL.prefix(string));
            return;
        }

        for(OfflinePlayer offlinePlayer : memberService.getOfflinePlayers(settlement)) {
            if (!offlinePlayer.isOnline()) return;
            Player onlinePlayer = offlinePlayer.getPlayer();

            if (onlinePlayer == null) return;
            onlinePlayer.sendMessage(Message.JOIN_PLAYER.prefix(player.getName()));
        }

        Role role = roleService.getRole(settlement.getRoleId());
        if(role == null) {
            player.sendMessage(Message.ROLE_DEFAULT.prefix(settlement.getName()));
            return;
        }

        borderService.removePlayer(player);
        invitationService.removeInvitation(player, settlement);
        trustService.unTrust(player, settlement);
        memberService.addMember(player, role, settlement);
        player.sendMessage(Message.JOIN.prefix(string));

        Chunk chunk = player.getLocation().getChunk();
        Claim claim = claimService.getClaim(settlement, chunk);
        if(claim == null) return;

        borderService.addPlayer(player, settlement, Color.GREEN);
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (strings.length == 2) {
            Set<Long> invitations = invitationService.getInvitations(player);
            Set<Settlement> settlements = settlementService.getSettlements(invitations);
            if(invitations != null) return settlements.stream().map(Settlement::getName).toList();
        }
        return List.of();
    }
}