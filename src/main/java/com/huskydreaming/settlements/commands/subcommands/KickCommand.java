package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.BorderService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

@Command(label = CommandLabel.KICK, arguments = " [player]")
public class KickCommand implements CommandInterface {

    private final BorderService borderService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public KickCommand() {
        borderService = ServiceProvider.Provide(BorderService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        roleService = ServiceProvider.Provide(RoleService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if(strings.length == 2) {
            String string = strings[1];
            OfflinePlayer offlinePlayer = Remote.getOfflinePlayer(string);
            if (offlinePlayer == null) {
                player.sendMessage(Remote.prefix(Locale.PLAYER_NULL, string));
                return;
            }

            if (!memberService.hasSettlement(player)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
                return;
            }

            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            Role role = roleService.getRole(settlement, member);

            if (!(role.hasPermission(RolePermission.MEMBER_KICK) || settlement.isOwner(player))) {
                player.sendMessage(Remote.prefix(Locale.NO_PERMISSIONS, RolePermission.MEMBER_KICK.getName()));
                return;
            }

            if(role.hasPermission(RolePermission.MEMBER_KICK_EXEMPT) || settlement.isOwner(offlinePlayer)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_KICK_EXEMPT));
            } else {
                String settlementName = settlement.getName();

                memberService.remove(offlinePlayer);
                borderService.removePlayer(player);
                borderService.addPlayer(player, settlementName, Color.RED);

                Player onlinePlayer = offlinePlayer.getPlayer();
                if(onlinePlayer != null) onlinePlayer.sendMessage(Remote.prefix(Locale.SETTLEMENT_KICK));

                List<OfflinePlayer> offlinePlayers = memberService.getOfflinePlayers(settlement);
                offlinePlayers.forEach(off ->  {
                    if(off.isOnline()) {
                        Player on = off.getPlayer();
                        if(on != null) on.sendMessage(Remote.prefix(Locale.SETTLEMENT_LEAVE_PLAYER, offlinePlayer.getName()));
                    }
                });
            }
        }
    }
}
