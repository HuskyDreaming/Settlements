package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.InvitationService;
import com.huskydreaming.settlements.services.SettlementService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.INVITE)
public class InviteCommand implements CommandInterface {

    @Inject
    private InvitationService invitationService;

    @Inject
    private SettlementService settlementService;


    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            Player target = Bukkit.getPlayer(strings[1]);
            if (target == null) {
                player.sendMessage("The player " + strings[1] + " does not seem to be online.");
                return;
            }

            if (!settlementService.hasSettlement(player)) {
                player.sendMessage("You do not seem to belong to a settlement.");
                return;
            }

            if (settlementService.hasSettlement(target)) {
                player.sendMessage("The player " + target.getName() + " already has a settlement.");
                return;
            }

            Settlement settlement = settlementService.getSettlement(player);
            Role role = settlement.getRole(player);

            if (!(role.hasPermission(RolePermission.CITIZEN_INVITE) || settlement.isOwner(player))) {
                player.sendMessage("You do not have permissions.");
                return;
            }

            invitationService.sendInvitation(target, settlement);
            player.sendMessage("You have sent an invitation to " + target.getName() + ".");
        }

    }
}
