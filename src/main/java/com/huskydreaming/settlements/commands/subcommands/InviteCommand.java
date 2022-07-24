package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Citizen;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.CitizenService;
import com.huskydreaming.settlements.services.InvitationService;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.INVITE)
public class InviteCommand implements CommandInterface {

    @Inject
    private CitizenService citizenService;

    @Inject
    private InvitationService invitationService;

    @Inject
    private SettlementService settlementService;

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            Player target = Bukkit.getPlayer(strings[1]);
            if (target == null) {
                player.sendMessage(Remote.prefix(Locale.PLAYER_OFFLINE, strings[1]));
                return;
            }

            if (!citizenService.hasSettlement(player)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
                return;
            }

            if (target == player) {
                player.sendMessage(Remote.prefix(Locale.INVITATION_SELF));
                return;
            }

            if (citizenService.hasSettlement(target)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_HAS_SETTLEMENT, target.getName()));
                return;
            }

            Citizen citizen = citizenService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(citizen.getSettlement());
            Role role = settlement.getRole(citizen.getRole());

            if (!(role.hasPermission(RolePermission.CITIZEN_INVITE) || settlement.isOwner(player))) {
                player.sendMessage(Remote.prefix(Locale.NO_PERMISSIONS, RolePermission.CITIZEN_INVITE.getName()));
                return;
            }

            invitationService.sendInvitation(target, settlement);
            player.sendMessage(Remote.prefix(Locale.INVITATION_SENT), target.getName());
        }
    }
}
