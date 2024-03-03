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
    private final SettlementService settlementService;
    private final RoleService roleService;

    public SettlementsInventory(SettlementPlugin plugin, int rows, String[] settlementNames) {
        super(rows, settlementNames);

        claimService = plugin.provide(ClaimService.class);
        memberService = plugin.provide(MemberService.class);
        settlementService = plugin.provide(SettlementService.class);
        roleService = plugin.provide(RoleService.class);
    }

    @Override
    public ItemStack construct(int index, String settlementName) {

        int roles = roleService.getRoles(settlementName).size();
        int claims = claimService.getChunks(settlementName).size();
        int members = memberService.getMembers(settlementName).size();

        Settlement settlement = settlementService.getSettlement(settlementName);
        Material icon = settlement.getIcon();

        return ItemBuilder.create()
                .setDisplayName(Remote.parameterize(Menu.CLAIMS_TITLE, index, settlementName))
                .setLore(Remote.parameterizeList(Menu.SETTLEMENT_LORE,
                        settlement.getOwnerName(),
                        members, settlement.getMaxCitizens(),
                        claims, settlement.getMaxLand(),
                        roles, settlement.getMaxRoles()))
                .setMaterial(icon == null ? Material.GRASS_BLOCK : icon)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, String name, InventoryContents contents) {

    }
}
