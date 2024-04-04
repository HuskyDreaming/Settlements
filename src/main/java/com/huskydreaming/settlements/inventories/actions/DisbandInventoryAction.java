package com.huskydreaming.settlements.inventories.actions;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.settlements.inventories.base.InventoryAction;
import com.huskydreaming.settlements.inventories.base.InventoryActionType;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.types.Locale;
import org.bukkit.entity.Player;

public class DisbandInventoryAction implements InventoryAction {

    private final BorderService borderService;
    private final FlagService flagService;
    private final MemberService memberService;
    private final ClaimService claimService;
    private final RoleService roleService;
    private final SettlementService settlementService;
    private final TrustService trustService;
    private final String settlement;

    public DisbandInventoryAction(HuskyPlugin plugin, String settlement) {
        borderService = plugin.provide(BorderService.class);
        flagService = plugin.provide(FlagService.class);
        settlementService = plugin.provide(SettlementService.class);
        claimService = plugin.provide(ClaimService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        trustService = plugin.provide(TrustService.class);

        this.settlement = settlement;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.DISBAND;
    }

    @Override
    public void onAccept(Player player) {
        claimService.clean(settlement);
        memberService.clean(settlement);
        flagService.clean(settlement);
        roleService.clean(settlement);
        trustService.clean(settlement);
        settlementService.disbandSettlement(settlement);
        borderService.removePlayer(player);

        player.closeInventory();
        player.sendMessage(Locale.SETTLEMENT_DISBAND_YES.prefix());
    }

    @Override
    public void onDeny(Player player) {
        player.closeInventory();
        player.sendMessage(Locale.SETTLEMENT_DISBAND_NO.prefix());
    }
}