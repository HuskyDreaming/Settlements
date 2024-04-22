package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.types.Message;
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
    private final SettlementService settlementService;
    private final TrustService trustService;

    public AcceptCommand(HuskyPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        claimService = plugin.provide(ClaimService.class);
        memberService = plugin.provide(MemberService.class);
        invitationService = plugin.provide(InvitationService.class);
        settlementService = plugin.provide(SettlementService.class);
        trustService = plugin.provide(TrustService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 2) return;
        String string = strings[1];
        if (invitationService.hasNoInvitation(player, string)) {
            player.sendMessage(Message.INVITATION_NULL.prefix(string));
            return;
        }

        Settlement settlement = settlementService.getSettlement(string);
        if (settlement == null) {
            player.sendMessage(Message.NULL.prefix(string));
            return;
        }

        for(OfflinePlayer offlinePlayer : memberService.getOfflinePlayers(string)) {
            if (!offlinePlayer.isOnline()) return;
            Player onlinePlayer = offlinePlayer.getPlayer();

            if (onlinePlayer == null) return;
            onlinePlayer.sendMessage(Message.JOIN_PLAYER.prefix(player.getName()));
        }

        borderService.removePlayer(player);
        invitationService.removeInvitation(player, string);
        trustService.unTrust(player, string);

        memberService.add(player, string, settlement.getDefaultRole());
        player.sendMessage(Message.JOIN.prefix(string));

        Chunk chunk = player.getLocation().getChunk();
        if (!claimService.isClaim(chunk)) return;

        String claim = claimService.getClaim(player.getLocation().getChunk());
        borderService.addPlayer(player, claim, claim.equalsIgnoreCase(string) ? Color.AQUA : Color.RED);
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (strings.length == 2) {
            Set<String> invitations = invitationService.getInvitations(player);
            if(invitations != null) return invitations.stream().toList();
        }
        return List.of();
    }
}