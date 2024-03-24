package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.Locale;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.DISBAND)
public class DisbandCommand implements SubCommand {

    private final BorderService borderService;
    private final MemberService memberService;
    private final ChunkService chunkService;
    private final RoleService roleService;
    private final FlagService flagService;
    private final SettlementService settlementService;

    public DisbandCommand(HuskyPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        memberService = plugin.provide(MemberService.class);
        chunkService = plugin.provide(ChunkService.class);
        roleService = plugin.provide(RoleService.class);
        flagService = plugin.provide(FlagService.class);
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
        if (!settlement.isOwner(player)) {
            player.sendMessage(Locale.SETTLEMENT_NOT_OWNER.prefix());
            return;
        }

        flagService.clean(member.getSettlement());
        chunkService.clean(member.getSettlement());
        memberService.clean(member.getSettlement());
        roleService.clean(member.getSettlement());
        settlementService.disbandSettlement(member.getSettlement());
        borderService.removePlayer(player);

        player.sendMessage(Locale.SETTLEMENT_DISBAND.prefix());
    }
}