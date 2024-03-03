package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Collection;

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
            if(memberService.hasSettlement(player)) {
                Member member = memberService.getCitizen(player);
                Settlement settlement = settlementService.getSettlement(member.getSettlement());
                Collection<Chunk> chunks = claimService.getChunks(member.getSettlement());
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
