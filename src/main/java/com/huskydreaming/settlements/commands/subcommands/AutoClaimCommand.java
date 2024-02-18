package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Collection;

@Command(label = CommandLabel.AUTOCLAIM, requiresPermissions = true)
public class AutoClaimCommand implements CommandInterface {

    private final ClaimService claimService;
    private final MemberService memberService;
    private final SettlementService settlementService;

    public AutoClaimCommand() {
        claimService = ServiceProvider.Provide(ClaimService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 1) {
            if(memberService.hasSettlement(player)) {
                Member member = memberService.getCitizen(player);
                Settlement settlement = settlementService.getSettlement(member.getSettlement());
                Collection<Chunk> chunks = claimService.getChunks(settlement);
                if(chunks.size() >= settlement.getMaxLand()) {
                    player.sendMessage(Remote.prefix(Locale.SETTLEMENT_AUTO_CLAIM_ON_MAX_LAND));
                    return;
                }

                boolean autoClaim = member.hasAutoClaim();

                player.sendMessage(Remote.prefix(autoClaim ? Locale.SETTLEMENT_AUTO_CLAIM_OFF : Locale.SETTLEMENT_AUTO_CLAIM_ON));
                member.setAutoClaim(!autoClaim);
            } else {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
            }
        }
    }
}
