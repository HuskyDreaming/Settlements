package com.huskydreaming.settlements.inventories.actions;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.settlements.database.entities.Container;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.inventories.base.InventoryAction;
import com.huskydreaming.settlements.inventories.base.InventoryActionType;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.entity.Player;

import java.util.Set;

public class DisbandInventoryAction implements InventoryAction {

    private final BorderService borderService;
    private final ClaimService claimService;
    private final ContainerService containerService;
    private final FlagService flagService;
    private final HomeService homeService;
    private final MemberService memberService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final SettlementService settlementService;
    private final TrustService trustService;
    private final long settlementId;

    public DisbandInventoryAction(HuskyPlugin plugin, Settlement settlement) {
        borderService = plugin.provide(BorderService.class);
        containerService = plugin.provide(ContainerService.class);
        claimService = plugin.provide(ClaimService.class);
        flagService = plugin.provide(FlagService.class);
        homeService = plugin.provide(HomeService.class);
        memberService = plugin.provide(MemberService.class);
        permissionService = plugin.provide(PermissionService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
        trustService = plugin.provide(TrustService.class);
        this.settlementId = settlement.getId();
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.DISBAND;
    }

    @Override
    public void onAccept(Player player) {
        Settlement settlement = settlementService.getSettlement(settlementId);
        if(settlement == null) return;

        Set<Role> roles = roleService.getRoles(settlement);
        Container container = containerService.getContainer(settlement);
        containerService.deleteContainer(container);

        claimService.clean(settlement);
        flagService.clean(settlement);
        homeService.clean(settlement);
        memberService.clean(settlement);
        permissionService.clean(roles);
        roleService.clean(settlement);
        trustService.clean(settlement);
        settlementService.disbandSettlement(settlement);
        borderService.removePlayer(player);

        player.closeInventory();
        player.sendMessage(Message.DISBAND_YES.prefix());
    }

    @Override
    public void onDeny(Player player) {
        player.closeInventory();
        player.sendMessage(Message.DISBAND_NO.prefix());
    }
}