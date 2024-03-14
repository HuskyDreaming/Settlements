package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Claim;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import org.bukkit.entity.Player;

import java.util.Set;

@Command(label = CommandLabel.AUTOCLAIM)
public class AutoClaimCommand implements CommandInterface {

    private final ClaimService claimService;
    private final MemberService memberService;
    private final SettlementService settlementService;

    public AutoClaimCommand(SettlementPlugin plugin) {
        claimService = plugin.provide(ClaimService.class);
        memberService = plugin.provide(MemberService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 1) {
            if (memberService.hasSettlement(player)) {
                Member member = memberService.getCitizen(player);
                Settlement settlement = settlementService.getSettlement(member.getSettlement());
                Set<Claim> claims = claimService.getClaims(member.getSettlement());
                if (claims.size() >= settlement.getMaxLand()) {
                    player.sendMessage(Locale.SETTLEMENT_AUTO_CLAIM_ON_MAX_LAND.prefix());
                    return;
                }

                boolean autoClaim = member.hasAutoClaim();
                Locale locale = autoClaim ? Locale.SETTLEMENT_AUTO_CLAIM_OFF : Locale.SETTLEMENT_AUTO_CLAIM_ON;

                player.sendMessage(locale.prefix());
                member.setAutoClaim(!autoClaim);
            } else {
                player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
            }
        }
    }
}