package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import com.huskydreaming.settlements.storage.enumerations.Menu;
import com.huskydreaming.settlements.utilities.Remote;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SettlementsInventory extends InventoryPageProvider<String> {

    private final ClaimService claimService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public SettlementsInventory(SettlementPlugin plugin, int rows, String[] settlementNames) {
        super(rows, settlementNames);

        memberService = plugin.provide(MemberService.class);
        claimService = plugin.provide(ClaimService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public ItemStack construct(int index, String settlementName) {

        Settlement settlement = settlementService.getSettlement(settlementName);
        Material icon = settlement.getIcon();

        int roles = roleService.getRoles(settlementName).size();
        int claims = claimService.getClaims(settlementName).size();
        int members = memberService.getMembers(settlementName).size();

        return ItemBuilder.create()
                .setDisplayName(Menu.CLAIMS_TITLE.parameterize(index, Remote.capitalizeFully(settlementName)))
                .setLore(Menu.SETTLEMENT_LORE.parameterizeList(
                        settlement.getOwnerName(),
                        members, settlement.getMaxCitizens(),
                        claims, settlement.getMaxLand(),
                        roles, settlement.getMaxRoles(),
                        settlement.getOwnerName()))
                .setMaterial(icon == null ? Material.GRASS_BLOCK : icon)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, String name, InventoryContents contents) {

    }
}