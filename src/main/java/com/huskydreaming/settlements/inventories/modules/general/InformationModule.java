package com.huskydreaming.settlements.inventories.modules.general;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.database.entities.Container;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InformationModule implements InventoryModule {

    private final ClaimService claimService;
    private final ContainerService containerService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public InformationModule(HuskyPlugin plugin) {
        this.claimService = plugin.provide(ClaimService.class);
        this.containerService = plugin.provide(ContainerService.class);
        this.memberService = plugin.provide(MemberService.class);
        this.roleService = plugin.provide(RoleService.class);
        this.settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public ItemStack itemStack(Player player) {
        if (!memberService.hasSettlement(player)) {
            player.closeInventory();
            return null;
        }

        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);
        Container container = containerService.getContainer(settlement);

        int members = memberService.getMembers(settlement).size();
        int claims = claimService.getClaims(settlement).size();
        int roles = roleService.getRoles(settlement).size();

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(settlement.getOwnerUUID());

        return ItemBuilder.create()
                .setMaterial(Material.CHEST)
                .setDisplayName(Menu.SETTLEMENT_INFO_TITLE.parse())
                .setLore(Menu.SETTLEMENT_INFO_LORE.parameterizeList(
                        settlement.getDescription(),
                        offlinePlayer.getName(),
                        members, container.getMaxMembers(),
                        claims, container.getMaxClaims(),
                        roles, container.getMaxRoles()
                )).build();
    }
}