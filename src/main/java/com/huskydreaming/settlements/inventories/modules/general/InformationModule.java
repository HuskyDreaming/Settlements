package com.huskydreaming.settlements.inventories.modules.general;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.Permission;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.types.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InformationModule implements InventoryModule {

    private final ClaimService claimService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public InformationModule(HuskyPlugin plugin) {
        this.claimService = plugin.provide(ClaimService.class);
        this.memberService = plugin.provide(MemberService.class);
        this.roleService = plugin.provide(RoleService.class);
        this.settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public ItemStack itemStack(Player player) {
        if (memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());

            int members = memberService.getMembers(member.getSettlement()).size();
            int claims = claimService.getClaims(member.getSettlement()).size();
            int roles = roleService.getRoles(member.getSettlement()).size();

            return ItemBuilder.create()
                    .setDisplayName(Menu.SETTLEMENT_INFO_TITLE.parse())
                    .setLore(Menu.SETTLEMENT_INFO_LORE.parameterizeList(
                            settlement.getDescription(),
                            settlement.getOwnerName(),
                            members, settlement.getMaxCitizens(),
                            claims, settlement.getMaxLand(),
                            roles, settlement.getMaxRoles()
                    )).setMaterial(Material.CHEST).build();
        } else {
            player.closeInventory();
            return null;
        }
    }

    @Override
    public Permission getPermission() {
        return RolePermission.DEFAULT;
    }
}