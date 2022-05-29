package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.commands.CommandBase;
import com.huskydreaming.settlements.managers.SettlementManager;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InviteCommand extends CommandBase {

    public InviteCommand() {
        super("invite");
    }

    @Override
    public void run(Settlements settlements, Player player, String[] strings) {
        if (strings.length == 2) {
            Player target = Bukkit.getPlayer(strings[1]);
            if (target == null) {
                player.sendMessage("The player " + strings[1] + " does not seem to be online.");
                return;
            }

            SettlementManager settlementManager = settlements.getSettlementManager();
            if (!settlementManager.hasSettlement(player)) {
                player.sendMessage("You do not seem to belong to a settlement.");
                return;
            }

            if (settlementManager.hasSettlement(target)) {
                player.sendMessage("The player " + target.getName() + " already has a settlement.");
                return;
            }

            Settlement settlement = settlementManager.getSettlement(player);
            Role role = settlement.getRole(player);

            if (!(role.hasPermission(RolePermission.CITIZEN_INVITE) || settlement.isOwner(player))) {
                player.sendMessage("You do not have permissions.");
                return;
            }

            settlements.getInvitationManager().sendInvitation(target, settlement);
            player.sendMessage("You have sent an invitation to " + target.getName() + ".");
        }

    }
}
