package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.database.entities.Container;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SettlementsInventory extends InventoryPageProvider<Long> {

    private final ClaimService claimService;
    private final ContainerService containerService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public SettlementsInventory(HuskyPlugin plugin, int rows, Long[] settlements) {
        super(rows, settlements);

        memberService = plugin.provide(MemberService.class);
        claimService = plugin.provide(ClaimService.class);
        containerService = plugin.provide(ContainerService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public ItemStack construct(Player player, int index, Long id) {

        Settlement settlement = settlementService.getSettlement(id);
        Container container = containerService.getContainer(settlement);

        int roles = roleService.getRoles(settlement).size();
        int claims = claimService.getClaims(settlement).size();
        int members = memberService.getMembers(settlement).size();

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(settlement.getOwnerUUID());

        return ItemBuilder.create()
                .setDisplayName(Menu.CLAIMS_TITLE.parameterize(index, Util.capitalize(settlement.getName())))
                .setLore(Menu.SETTLEMENT_LORE.parameterizeList(
                        offlinePlayer.getName(),
                        members, container.getMaxMembers(),
                        claims, container.getMaxClaims(),
                        roles, container.getMaxRoles()))
                .setMaterial(Material.GRASS_BLOCK)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, Long id, InventoryContents contents) {

    }
}