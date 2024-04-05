package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.storage.types.Locale;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Set;

@Command(label = CommandLabel.UN_TRUST)
public class UnTrustCommand implements SubCommand {

    private final BorderService borderService;
    private final ClaimService claimService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;
    private final TrustService trustService;

    public UnTrustCommand(HuskyPlugin plugin) {
        this.borderService = plugin.provide(BorderService.class);
        this.claimService = plugin.provide(ClaimService.class);
        this.memberService = plugin.provide(MemberService.class);
        this.roleService = plugin.provide(RoleService.class);
        this.settlementService = plugin.provide(SettlementService.class);
        this.trustService = plugin.provide(TrustService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            if (!memberService.hasSettlement(player)) {
                player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
                return;
            }

            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            Role role = roleService.getRole(member);

            if (!(role.hasPermission(RolePermission.MEMBER_TRUST) || settlement.isOwner(player))) {
                player.sendMessage(Locale.NO_PERMISSIONS.prefix(RolePermission.MEMBER_TRUST));
                return;
            }

            String string = strings[1];
            OfflinePlayer offlinePlayer = Util.getOfflinePlayer(string);
            if (offlinePlayer == null) {
                player.sendMessage(Locale.PLAYER_NULL.prefix(string));
                return;
            }

            if (memberService.hasSettlement(offlinePlayer)) {
                Member offlineMember = memberService.getCitizen(offlinePlayer);
                if (offlineMember.getSettlement().equalsIgnoreCase(member.getSettlement())) {
                    player.sendMessage(Locale.SETTLEMENT_IS_MEMBER.prefix(offlinePlayer.getName()));
                    return;
                }
            }

            Set<String> trustedSettlements = trustService.getSettlements(offlinePlayer);
            if (trustedSettlements != null && !trustedSettlements.contains(member.getSettlement())) {
                player.sendMessage(Locale.SETTLEMENT_IS_NOT_TRUSTED.prefix(offlinePlayer.getName()));
                return;
            }

            trustService.unTrust(offlinePlayer, member.getSettlement());
            player.sendMessage(Locale.SETTLEMENT_TRUST_REMOVE.prefix(offlinePlayer.getName()));

            if (!offlinePlayer.isOnline()) return;
            Player target = offlinePlayer.getPlayer();
            borderService.removePlayer(target);

            if (target == null) return;
            target.sendMessage(Locale.SETTLEMENT_TRUST_REMOVE_OFFLINE_PLAYER.prefix(member.getSettlement()));

            Chunk chunk = player.getLocation().getChunk();
            if (!claimService.isClaim(chunk)) return;

            String settlementName = claimService.getClaim(chunk);
            if (memberService.hasSettlement(offlinePlayer)) {
                Member targetMember = memberService.getCitizen(offlinePlayer);

                if (settlementName.equalsIgnoreCase(targetMember.getSettlement())) {
                    borderService.addPlayer(player, settlementName, Color.AQUA);
                } else {
                    borderService.addPlayer(player, settlementName, Color.RED);
                }
                return;
            }

            borderService.addPlayer(player, settlementName, Color.RED);
        }
    }
}