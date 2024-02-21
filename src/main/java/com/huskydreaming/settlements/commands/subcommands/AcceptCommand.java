package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

@Command(label = CommandLabel.ACCEPT, arguments = " [settlement]")
public class AcceptCommand implements CommandInterface {

    private final BorderService borderService;
    private final ClaimService claimService;
    private final MemberService memberService;
    private final InvitationService invitationService;
    private final SettlementService settlementService;

    public AcceptCommand() {
        borderService = ServiceProvider.Provide(BorderService.class);
        claimService = ServiceProvider.Provide(ClaimService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        invitationService = ServiceProvider.Provide(InvitationService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            String string = strings[1];
            if (invitationService.hasNoInvitation(player, string)) {
                player.sendMessage(Remote.prefix(Locale.INVITATION_NULL, string));
                return;
            }

            Settlement settlement = settlementService.getSettlement(string);
            if (settlement == null) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_NULL, string));
                return;
            }

            List<OfflinePlayer> offlinePlayers = memberService.getOfflinePlayers(settlement);
            offlinePlayers.forEach(offlinePlayer ->  {
                if(offlinePlayer.isOnline()) {
                    Player onlinePlayer = offlinePlayer.getPlayer();
                    if(onlinePlayer != null) onlinePlayer.sendMessage(Remote.prefix(Locale.SETTLEMENT_JOIN_PLAYER, player.getName()));
                }
            });

            invitationService.removeInvitation(player, string);
            memberService.add(player, settlement);
            borderService.removePlayer(player);

            Chunk chunk = player.getLocation().getChunk();
            if(claimService.isClaim(chunk)) {
                String claim = claimService.getClaim(player.getLocation().getChunk());
                if(claim.equalsIgnoreCase(settlement.getName())) {
                    borderService.addPlayer(player, claim, Color.AQUA);
                } else {
                    borderService.addPlayer(player, claim, Color.RED);
                }
            }

            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_JOIN, string));
        }
    }
}
