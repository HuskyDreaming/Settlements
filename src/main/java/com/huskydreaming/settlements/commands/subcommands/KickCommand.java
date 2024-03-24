package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.BorderService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.Locale;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

@Command(label = CommandLabel.KICK, arguments = " [player]")
public class KickCommand implements SubCommand {

    private final BorderService borderService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public KickCommand(SettlementPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            String string = strings[1];
            OfflinePlayer offlinePlayer = Util.getOfflinePlayer(string);
            if (offlinePlayer == null) {
                player.sendMessage(Locale.PLAYER_NULL.prefix(string));
                return;
            }

            if (!memberService.hasSettlement(player)) {
                player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
                return;
            }

            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            Role role = roleService.getRole(member);

            if (!(role.hasPermission(RolePermission.MEMBER_KICK) || settlement.isOwner(player))) {
                player.sendMessage(Locale.NO_PERMISSIONS.prefix(RolePermission.MEMBER_KICK.getName()));
                return;
            }

            if (role.hasPermission(RolePermission.MEMBER_KICK_EXEMPT) || settlement.isOwner(offlinePlayer)) {
                player.sendMessage(Locale.SETTLEMENT_KICK_EXEMPT.prefix());
            } else {
                memberService.remove(offlinePlayer);
                borderService.removePlayer(player);
                borderService.addPlayer(player, member.getSettlement(), Color.RED);

                Player onlinePlayer = offlinePlayer.getPlayer();
                if (onlinePlayer != null) onlinePlayer.sendMessage(Locale.SETTLEMENT_KICK.prefix());

                List<OfflinePlayer> offlinePlayers = memberService.getOfflinePlayers(member.getSettlement());
                offlinePlayers.forEach(off -> {
                    if (off.isOnline()) {
                        Player on = off.getPlayer();
                        if (on != null) {
                            on.sendMessage(Locale.SETTLEMENT_LEAVE_PLAYER.prefix(offlinePlayer.getName()));
                        }
                    }
                });
            }
        }
    }
}