package com.huskydreaming.settlements.inventories.providers;

import com.google.inject.Inject;
import com.huskydreaming.settlements.inventories.InventoryItem;
import com.huskydreaming.settlements.persistence.Citizen;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.CitizenService;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SettlementInventory implements InventoryProvider {

    @Inject
    private CitizenService citizenService;

    @Inject
    private SettlementService settlementService;

    private final Settlement settlement;
    private Role role;

    public SettlementInventory(Settlement settlement) {
        this.settlement = settlement;
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        Citizen citizen = citizenService.getCitizen(player);
        role = settlement.getRole(citizen.getRole());

        contents.fillBorders(InventoryItem.border());
        /*
        contents.set(1, 1, citizensItem(player, settlement));
        contents.set(1, 2, roleItem(player, settlement));
        contents.set(1, 3, landItem(player, settlement));
         */
        contents.set(1, 4, spawn(player, settlement));
        contents.set(1, 7, disband(player, settlement, contents));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

/*
    private ClickableItem citizensItem(Player player, Settlement settlement) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.CITIZENS_TITLE.parse())
                .setLore(Menu.CITIZENS_LORE.parseList())
                .setMaterial(Material.EMERALD)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_CITIZENS) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e -> inventoryService.getCitizensInventory(settlement).open(player));
    }

    private ClickableItem roleItem(Player player, Settlement settlement) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.ROLES_TITLE.parse())
                .setLore(Menu.ROLES_LORE.parseList())
                .setMaterial(Material.WRITABLE_BOOK)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_ROLES) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e -> inventoryService.getRolesInventory(settlement).open(player));
    }


    private ClickableItem landItem(Player player, Settlement settlement) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(ChatColor.GREEN + "Lands")
                .setLore(ChatColor.GRAY + "Click to see lands.")
                .setMaterial(Material.GRASS_BLOCK)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_LAND) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e -> inventoryService.getClaimsInventory(settlement).open(player));
    }
 */

    private ClickableItem spawn(Player player, Settlement settlement) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(ChatColor.GREEN + "Spawn")
                .setLore(ChatColor.GRAY + "Click to set spawn.")
                .setMaterial(Material.ENDER_PEARL)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_SPAWN) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e -> settlement.setLocation(player.getLocation()));
    }

    private ClickableItem disband(Player player, Settlement settlement, InventoryContents contents) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(ChatColor.RED + "Disband")
                .setLore(ChatColor.GRAY + "Click to disband settlement.")
                .setMaterial(Material.TNT_MINECART)
                .build();

        return InventoryItem.of(settlement.isOwner(player), itemStack, e -> {
            player.sendMessage("You have disbanded your settlement.");
            settlementService.disbandSettlement(settlement);
            contents.inventory().close(player);
        });
    }
}
