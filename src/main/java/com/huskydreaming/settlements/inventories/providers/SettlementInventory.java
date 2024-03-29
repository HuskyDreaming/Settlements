package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.inventories.InventoryAction;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.Locale;
import com.huskydreaming.settlements.storage.Menu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SettlementInventory implements InventoryProvider {

    private final HuskyPlugin plugin;
    private final MemberService memberService;
    private final ChunkService chunkService;
    private final InventoryService inventoryService;
    private final RoleService roleService;
    private final SettlementService settlementService;
    private final String settlementName;
    private final boolean teleportation;

    public SettlementInventory(HuskyPlugin plugin, String settlementName) {
        this.plugin = plugin;

        ConfigService configService =  plugin.provide(ConfigService.class);
        teleportation = configService.deserializeTeleportation(plugin);

        chunkService = plugin.provide(ChunkService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        inventoryService = plugin.provide(InventoryService.class);
        settlementService = plugin.provide(SettlementService.class);

        this.settlementName = settlementName;
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Role role = roleService.getRole(member);

        contents.fillBorders(InventoryItem.border());

        contents.set(1, 1, citizensItem(player, settlement, role));
        contents.set(1, 2, roleItem(player, settlement, role));
        contents.set(1, 3, claimItem(player, settlement, role));
        contents.set(1, 4, infoItem(settlement));
        contents.set(1, 5, flagItem(player, settlement, role));
        contents.set(1, 7, disbandItem(player, settlement));

        if(teleportation) contents.set(1, 6, spawn(player, settlement, role));
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
        return InventoryItem.of(permission, itemStack, e -> inventoryService.getCitizensInventory(plugin, settlementName).open(player));
    }

    private ClickableItem roleItem(Player player, Settlement settlement, Role role) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_ROLES_TITLE.parse())
                .setLore(Menu.SETTLEMENT_ROLES_LORE.parseList())
                .setMaterial(Material.ANVIL)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_ROLES) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e -> inventoryService.getRolesInventory(plugin, settlementName).open(player));
    }


    private ClickableItem claimItem(Player player, Settlement settlement, Role role) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_CLAIMS_TITLE.parse())
                .setLore(Menu.SETTLEMENT_CLAIMS_LORE.parseList())
                .setMaterial(Material.GRASS_BLOCK)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_LAND) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e -> inventoryService.getClaimsInventory(plugin, settlementName).open(player));
    }

    private ClickableItem infoItem(Settlement settlement) {

        int roles = roleService.getRoles(settlementName).size();
        int claims = chunkService.getClaims(settlementName).size();
        int members = memberService.getMembers(settlementName).size();

        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_INFO_TITLE.parse())
                .setLore(Menu.SETTLEMENT_INFO_LORE.parameterizeList(
                        settlement.getDescription(),
                        settlement.getOwnerName(),
                        members, settlement.getMaxCitizens(),
                        claims, settlement.getMaxLand(),
                        roles, settlement.getMaxRoles()
                )).setMaterial(Material.CHEST).build();

        return ClickableItem.empty(itemStack);
    }

    private ClickableItem flagItem(Player player, Settlement settlement, Role role) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_FLAGS_TITLE.parse())
                .setLore(Menu.SETTLEMENT_FLAGS_LORE.parseList())
                .setMaterial(Material.WRITABLE_BOOK)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_FLAGS) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e -> inventoryService.getFlagsInventory(plugin, settlementName).open(player));
    }

    private ClickableItem spawn(Player player, Settlement settlement, Role role) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_SPAWN_TITLE.parse())
                .setLore(Menu.SETTLEMENT_SPAWN_LORE.parseList())
                .setMaterial(Material.ENDER_PEARL)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_SPAWN) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e -> {
            if (e.isLeftClick()) {
                player.closeInventory();
                player.teleport(settlement.getLocation());
            } else if (e.isRightClick()) {
                settlement.setLocation(player.getLocation());
                player.closeInventory();
                player.sendMessage(Locale.SETTLEMENT_SET_SPAWN.prefix());
            }
        });
    }

    private ClickableItem disbandItem(Player player, Settlement settlement) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_DISBAND_TITLE.parse())
                .setLore(Menu.SETTLEMENT_DISBAND_LORE.parseList())
                .setMaterial(Material.TNT_MINECART)
                .build();

        return InventoryItem.of(settlement.isOwner(player), itemStack, e -> inventoryService.getConfirmationInventory(plugin, settlementName, InventoryAction.DISBAND).open(player));
    }
}