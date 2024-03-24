package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.Locale;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.UN_CLAIM)
public class UnClaimCommand implements SubCommand {

    private final BorderService borderService;
    private final MemberService memberService;
    private final ChunkService chunkService;
    private final RoleService roleService;

    private final SettlementService settlementService;

    public UnClaimCommand(HuskyPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        memberService = plugin.provide(MemberService.class);
        chunkService = plugin.provide(ChunkService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Role role = roleService.getRole(member);
        if (!(role.hasPermission(RolePermission.LAND_UNCLAIM) || settlement.isOwner(player))) {
            player.sendMessage(Locale.NO_PERMISSIONS.prefix(RolePermission.LAND_UNCLAIM.getName()));
            return;
        }

        Chunk chunk = player.getLocation().getChunk();
        String claim = chunkService.getClaim(chunk);
        if (claim == null) {
            player.sendMessage(Locale.SETTLEMENT_LAND_NOT_CLAIMED.prefix());
            return;
        }


        if (chunkService.getClaims(member.getSettlement()).size() == 1) {
            player.sendMessage(Locale.SETTLEMENT_LAND_UNCLAIM_ONE.prefix());
            return;
        }

        if (member.getSettlement().equalsIgnoreCase(claim)) {
            chunkService.removeClaim(chunk);
            borderService.removePlayer(player);
            player.sendMessage(Locale.SETTLEMENT_LAND_UNCLAIM.prefix());
            return;
        }

        player.sendMessage(Locale.SETTLEMENT_LAND_CLAIMED.prefix());
    }
}