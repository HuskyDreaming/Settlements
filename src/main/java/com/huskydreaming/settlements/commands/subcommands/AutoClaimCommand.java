package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.huskycore.data.ChunkData;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.enumeration.types.SettlementDefaultType;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.types.Locale;
import org.bukkit.entity.Player;

import java.util.Set;

@CommandAnnotation(label = CommandLabel.AUTO_CLAIM)
public class AutoClaimCommand implements PlayerCommandProvider {

    private final ClaimService claimService;
    private final ConfigService configService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public AutoClaimCommand(HuskyPlugin plugin) {
        claimService = plugin.provide(ClaimService.class);
        configService = plugin.provide(ConfigService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 1) return;

        if (configService.isDisabledWorld(player)) return;

        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Role role = roleService.getRole(member);

        if(!(role.hasPermission(RolePermission.CLAIM_LAND) || settlement.isOwner(player))) {
            player.sendMessage(Locale.NO_PERMISSIONS.prefix());
            return;
        }

        Set<ChunkData> chunks = claimService.getClaims(member.getSettlement());
        Config config = configService.getConfig();
        if (chunks.size() >= config.getSettlementDefault(SettlementDefaultType.MAX_CLAIMS)) {
            player.sendMessage(Locale.SETTLEMENT_AUTO_CLAIM_ON_MAX_LAND.prefix());
            return;
        }

        boolean autoClaim = member.hasAutoClaim();
        Locale locale = autoClaim ? Locale.SETTLEMENT_AUTO_CLAIM_OFF : Locale.SETTLEMENT_AUTO_CLAIM_ON;

        player.sendMessage(locale.prefix());
        member.setAutoClaim(!autoClaim);
    }
}