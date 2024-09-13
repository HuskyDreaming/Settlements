package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Permission;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Message;
import com.huskydreaming.settlements.enumeration.locale.Menu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class RoleInventory extends InventoryPageProvider<PermissionType> {

    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final PermissionService permissionService;
    private final SettlementService settlementService;
    private final RoleService roleService;
    private final Role role;

    public RoleInventory(HuskyPlugin plugin, int rows, Role role, PermissionType[] permissions) {
        super(rows, permissions);
        this.plugin = plugin;

        inventoryService = plugin.provide(InventoryService.class);
        memberService = plugin.provide(MemberService.class);
        permissionService = plugin.provide(PermissionService.class);
        settlementService = plugin.provide(SettlementService.class);
        roleService = plugin.provide(RoleService.class);

        this.role = role;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);

        contents.set(0, 0, InventoryItem.back(player, inventoryService.getRolesInventory(plugin, player)));
        contents.set(0, 1, defaultItem(player, settlement));
        contents.set(0, 2, deleteItem(player, settlement));
    }

    @Override
    public ItemStack construct(Player player, int index, PermissionType permissionType) {
        Set<PermissionType> permissions = permissionService.getPermissions(role);
        return InventoryItem.of(permissions.contains(permissionType), permissionType.toString(), permissionType.getDescription());
    }

    @Override
    public void run(InventoryClickEvent event, PermissionType permissionType, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if(!memberService.hasSettlement(player)) return;
            Set<PermissionType> permissions = permissionService.getPermissions(role);
            if (permissions.contains(permissionType)) {
                Permission permission = permissionService.getPermissions(role, permissionType);
                if(permission != null) {
                    permissionService.removePermission(permission, i -> contents.inventory().open(player));
                }
            } else {
                permissionService.addPermission(role, permissionType, i -> contents.inventory().open(player));
            }
        }
    }

    private ClickableItem deleteItem(Player player, Settlement settlement) {
        Role defaultRole = roleService.getRole(settlement.getRoleId());
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.ROLE_DELETE_TITLE.parse())
                .setLore(Menu.ROLE_DELETE_LORE.parseList())
                .setMaterial(Material.TNT_MINECART)
                .build(), e -> {
            if (roleService.getRoles(settlement).size() > 1) {
                if (defaultRole.getName().equalsIgnoreCase(role.getName())) {
                    Role otherRole = roleService.getOtherRole(settlement, role.getName());
                    settlement.setRoleId(otherRole.getId());
                }

                memberService.sync(settlement, role);
                roleService.removeRole(settlement, role.getName());
                inventoryService.getRolesInventory(plugin, player).open(player);
            } else {
                player.sendMessage(Message.ROLE_ONE.prefix());
                player.closeInventory();
            }
        });
    }

    private ClickableItem defaultItem(Player player, Settlement settlement) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.ROLE_DEFAULT_TITLE.parse())
                .setLore(Menu.ROLE_DEFAULT_LORE.parseList())
                .setMaterial(Material.DIAMOND)
                .build(), e -> {
            settlement.setRoleId(role.getId());
            inventoryService.getRolesInventory(plugin, player).open(player);
        });
    }
}