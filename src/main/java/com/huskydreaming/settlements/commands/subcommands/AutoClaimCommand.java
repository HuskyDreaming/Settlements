package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.database.entities.*;
import com.huskydreaming.settlements.database.persistence.Config;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.enumeration.types.SettlementDefaultType;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.entity.Player;

import java.util.Set;

@CommandAnnotation(label = CommandLabel.AUTO_CLAIM)
public class AutoClaimCommand implements PlayerCommandProvider {

    private final ClaimService claimService;
    private final ConfigService configService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final SettlementService settlementService;

    public AutoClaimCommand(HuskyPlugin plugin) {
        claimService = plugin.provide(ClaimService.class);
        configService = plugin.provide(ConfigService.class);
        permissionService = plugin.provide(PermissionService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 1) return;

        if (configService.isDisabledWorld(player)) return;

        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Message.PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);
        Role role = roleService.getRole(member);
        Set<PermissionType> permissions = permissionService.getPermissions(role);

        if (!(permissions.contains(PermissionType.CLAIM_LAND) || settlement.isOwner(player))) {
            player.sendMessage(Message.GENERAL_NO_PERMISSIONS.prefix());
            return;
        }


        Set<Claim> claims = claimService.getClaims(member.getSettlementId());
        Config config = configService.getConfig();
        if (claims.size() >= config.getSettlementDefault(SettlementDefaultType.MAX_CLAIMS)) {
            player.sendMessage(Message.AUTO_CLAIM_ON_MAX_LAND.prefix());
            return;
        }

        boolean autoClaim = member.isAutoClaim();
        Message message = autoClaim ? Message.AUTO_CLAIM_OFF : Message.AUTO_CLAIM_ON;

        player.sendMessage(message.prefix());
        member.setAutoClaim(!autoClaim);
    }
}