package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import com.huskydreaming.settlements.storage.enumerations.Menu;
import com.huskydreaming.settlements.utilities.Remote;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SettlementsInventory extends InventoryPageProvider<Settlement> {
    private final ClaimService claimService;
    private final MemberService memberService;
    private final RoleService roleService;

    public SettlementsInventory(int rows, Settlement[] settlements) {
        super(rows, settlements);

        claimService = ServiceProvider.Provide(ClaimService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        roleService = ServiceProvider.Provide(RoleService.class);
    }

    @Override
    public ItemStack construct(int index, Settlement settlement) {

        int roles = roleService.getRoles(settlement).size();
        int claims = claimService.getChunks(settlement).size();
        int members = memberService.getMembers(settlement).size();

        Material icon = settlement.getIcon();

        return ItemBuilder.create()
                .setDisplayName(Remote.parameterize(Menu.CLAIMS_TITLE, index, settlement.getName()))
                .setLore(Remote.parameterizeList(Menu.SETTLEMENT_LORE,
                        settlement.getOwnerName(),
                        members, settlement.getMaxCitizens(),
                        claims, settlement.getMaxLand(),
                        roles, settlement.getMaxRoles()))
                .setMaterial(icon == null ? Material.GRASS_BLOCK : icon)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, Settlement settlement, InventoryContents contents) {

    }
}
