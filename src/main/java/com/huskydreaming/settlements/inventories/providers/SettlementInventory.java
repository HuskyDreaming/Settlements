package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.settlements.inventories.InventoryItem;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Menu;
import com.huskydreaming.settlements.utilities.Remote;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SettlementInventory implements InventoryProvider {

    private final MemberService memberService;
    private final ClaimService claimService;
    private final InventoryService inventoryService;

    private final RoleService roleService;

    private final SettlementService settlementService;

    private final Settlement settlement;

    public SettlementInventory(Settlement settlement) {
        settlementService = ServiceProvider.Provide(SettlementService.class);
        claimService = ServiceProvider.Provide(ClaimService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        roleService = ServiceProvider.Provide(RoleService.class);
        inventoryService = ServiceProvider.Provide(InventoryService.class);

        this.settlement = settlement;
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        Member member = memberService.getCitizen(player);
        Role role = roleService.getRole(settlement, member);

        contents.fillBorders(InventoryItem.border());

        contents.set(1, 1, citizensItem(player, settlement, role));
        contents.set(1, 2, roleItem(player, settlement, role));
        contents.set(1, 3, landItem(player, settlement, role));
        contents.set(1, 4, info(player, settlement, role));
        contents.set(1, 5, spawn(player, settlement, role));
        contents.set(1, 7, disband(player, settlement, contents));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    private ClickableItem citizensItem(Player player, Settlement settlement, Role role) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_CITIZENS_TITLE.parse())
                .setLore(Menu.SETTLEMENT_CITIZENS_LORE.parseList())
                .setMaterial(Material.EMERALD)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_CITIZENS) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e -> inventoryService.getCitizensInventory(settlement).open(player));
    }

    private ClickableItem roleItem(Player player, Settlement settlement, Role role) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_ROLES_TITLE.parse())
                .setLore(Menu.SETTLEMENT_ROLES_LORE.parseList())
                .setMaterial(Material.WRITABLE_BOOK)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_ROLES) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e -> inventoryService.getRolesInventory(settlement).open(player));
    }


    private ClickableItem landItem(Player player, Settlement settlement, Role role) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_LANDS_TITLE.parse())
                .setLore(Menu.SETTLEMENT_LANDS_LORE.parseList())
                .setMaterial(Material.GRASS_BLOCK)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_LAND) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e -> inventoryService.getClaimsInventory(settlement).open(player));
    }

    private ClickableItem info(Player player, Settlement settlement, Role role) {
        int roles = roleService.getRoles(settlement).size();
        int claims = claimService.getChunks(settlement).size();
        int members = memberService.getMembers(settlement).size();

        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_INFO_TITLE.parse())
                .setLore(Remote.parameterizeList(Menu.SETTLEMENT_INFO_LORE,
                        settlement.getDescription(),
                        settlement.getOwnerName(),
                        members, settlement.getMaxCitizens(),
                        claims, settlement.getMaxLand(),
                        roles, settlement.getMaxRoles()
                )).setMaterial(Material.CHEST).build();

        boolean permission = role.hasPermission(RolePermission.EDIT_SPAWN) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e -> settlement.setLocation(player.getLocation()));
    }

    private ClickableItem spawn(Player player, Settlement settlement, Role role) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_SPAWN_TITLE.parse())
                .setLore(Menu.SETTLEMENT_SPAWN_LORE.parseList())
                .setMaterial(Material.ENDER_PEARL)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_SPAWN) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e -> {
            if(e.isLeftClick()) {
                player.teleport(settlement.getLocation());
            } else if(e.isRightClick()) {
                settlement.setLocation(player.getLocation());
            }
        });
    }

    private ClickableItem disband(Player player, Settlement settlement, InventoryContents contents) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_DISBAND_TITLE.parse())
                .setLore(Menu.SETTLEMENT_DISBAND_LORE.parseList())
                .setMaterial(Material.TNT_MINECART)
                .build();

        return InventoryItem.of(settlement.isOwner(player), itemStack, e -> {
            player.sendMessage(Locale.SETTLEMENT_DISBAND.parse());

            claimService.clean(settlement);
            memberService.clean(settlement);
            settlementService.disbandSettlement(settlement);
            contents.inventory().close(player);
        });
    }
}
