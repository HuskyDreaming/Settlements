package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.types.Locale;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.DISBAND)
public class DisbandCommand implements SubCommand {

    private final BorderService borderService;
    private final MemberService memberService;
    private final ClaimService claimService;
    private final RoleService roleService;
    private final FlagService flagService;
    private final SettlementService settlementService;
    private final TrustService trustService;

    public DisbandCommand(HuskyPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        memberService = plugin.provide(MemberService.class);
        claimService = plugin.provide(ClaimService.class);
        roleService = plugin.provide(RoleService.class);
        flagService = plugin.provide(FlagService.class);
        settlementService = plugin.provide(SettlementService.class);
        trustService = plugin.provide(TrustService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        if (!settlement.isOwner(player)) {
            player.sendMessage(Locale.SETTLEMENT_NOT_OWNER.prefix());
            return;
        }

        flagService.clean(member.getSettlement());
        claimService.clean(member.getSettlement());
        memberService.clean(member.getSettlement());
        roleService.clean(member.getSettlement());
        trustService.clean(member.getSettlement());
        settlementService.disbandSettlement(member.getSettlement());
        borderService.removePlayer(player);

        player.sendMessage(Locale.SETTLEMENT_DISBAND.prefix());
    }
}