package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.database.entities.Claim;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

@CommandAnnotation(label = CommandLabel.UN_TRUST)
public class UnTrustCommand implements PlayerCommandProvider {

    private final BorderService borderService;
    private final ClaimService claimService;
    private final MemberService memberService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final SettlementService settlementService;
    private final TrustService trustService;

    public UnTrustCommand(HuskyPlugin plugin) {
        this.borderService = plugin.provide(BorderService.class);
        this.claimService = plugin.provide(ClaimService.class);
        this.memberService = plugin.provide(MemberService.class);
        this.permissionService = plugin.provide(PermissionService.class);
        this.roleService = plugin.provide(RoleService.class);
        this.settlementService = plugin.provide(SettlementService.class);
        this.trustService = plugin.provide(TrustService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 2) return;
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Message.PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);
        Role role = roleService.getRole(member);

        Set<PermissionType> permissions = permissionService.getPermissions(role);
        if(!(permissions.contains(PermissionType.MEMBER_TRUST) || settlement.isOwner(player))) {
            player.sendMessage(Message.GENERAL_NO_PERMISSIONS.prefix());
            return;
        }

        String string = strings[1];
        OfflinePlayer offlinePlayer = Util.getOfflinePlayer(string);
        if (offlinePlayer == null) {
            player.sendMessage(Message.PLAYER_NULL.prefix(string));
            return;
        }

        if (memberService.hasSettlement(offlinePlayer)) {
            Member offlineMember = memberService.getMember(offlinePlayer);
            if (offlineMember.getSettlementId() == member.getSettlementId()) {
                player.sendMessage(Message.MEMBER_ALREADY.prefix(offlinePlayer.getName()));
                return;
            }
        }

        Set<Long> settlementIds = trustService.getSettlementIds(offlinePlayer);
        if (settlementIds != null && !settlementIds.contains(member.getSettlementId())) {
            player.sendMessage(Message.TRUSTED_NOT.prefix(offlinePlayer.getName()));
            return;
        }

        trustService.unTrust(offlinePlayer, settlement);
        player.sendMessage(Message.TRUST_REMOVE.prefix(offlinePlayer.getName()));

        if (!offlinePlayer.isOnline()) return;
        Player target = offlinePlayer.getPlayer();
        borderService.removePlayer(target);

        if (target == null) return;
        target.sendMessage(Message.TRUST_REMOVE_OFFLINE_PLAYER.prefix(settlement.getName()));

        Chunk chunk = player.getLocation().getChunk();
        if (!claimService.isClaim(chunk)) return;

        Claim claim = claimService.getClaim(chunk);
        Settlement settlementClaim = settlementService.getSettlement(claim);
        if (memberService.hasSettlement(offlinePlayer)) {
            Member targetMember = memberService.getMember(offlinePlayer);

            if (claim.getSettlementId() == targetMember.getSettlementId()) {
                borderService.addPlayer(player, settlementClaim, Color.AQUA);
            } else {
                borderService.addPlayer(player, settlementClaim, Color.RED);
            }
        } else {
            borderService.addPlayer(player, settlementClaim, Color.RED);
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if(strings.length == 2 && memberService.hasSettlement(player)) {
            Member member = memberService.getMember(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlementId());
            return trustService.getOfflinePlayers(settlement).stream().map(OfflinePlayer::getName).toList();
        }
        return List.of();
    }
}