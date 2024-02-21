package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.DISBAND)
public class DisbandCommand implements CommandInterface {

    private final BorderService borderService;
    private final MemberService memberService;
    private final ClaimService claimService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public DisbandCommand() {
        borderService = ServiceProvider.Provide(BorderService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        claimService = ServiceProvider.Provide(ClaimService.class);
        roleService = ServiceProvider.Provide(RoleService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if(!memberService.hasSettlement(player)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        if(!settlement.isOwner(player)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_NOT_OWNER));
            return;
        }

        claimService.clean(settlement);
        memberService.clean(settlement);
        roleService.clean(settlement);
        settlementService.disbandSettlement(settlement);
        borderService.removePlayer(player);

        player.sendMessage(Remote.prefix(Locale.SETTLEMENT_DISBAND));
    }
}
