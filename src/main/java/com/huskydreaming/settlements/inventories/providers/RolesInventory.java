package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class RolesInventory extends InventoryPageProvider<Role> {

    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final PermissionService permissionService;
    private final SettlementService settlementService;
    private final RoleService roleService;

    public RolesInventory(HuskyPlugin plugin, int rows, Role[] roles) {
        super(rows, roles);
        this.plugin = plugin;

        memberService = plugin.provide(MemberService.class);
        inventoryService = plugin.provide(InventoryService.class);
        permissionService = plugin.provide(PermissionService.class);
        settlementService = plugin.provide(SettlementService.class);
        roleService = plugin.provide(RoleService.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        contents.set(0, 0, InventoryItem.back(player, inventoryService.getMainInventory(plugin, player)));
    }

    @Override
    public ItemStack construct(Player player, int index, Role role) {
        if (!memberService.hasSettlement(player)) {
            player.closeInventory();
            return null;
        }

        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);

        Role memberRole = roleService.getRole(member);
        Role defaultRole = roleService.getRole(settlement.getRoleId());

        boolean isDefault = defaultRole.getName().equalsIgnoreCase(role.getName());

        Material material = isDefault ? Material.BOOK : Material.PAPER;
        String roleDefault = isDefault ? Menu.SETTLEMENT_ROLE_EDIT_DEFAULT.parse() : "";

        List<String> lore = Menu.SETTLEMENT_ROLE_EDIT_LORE.parameterizeList();
        Set<PermissionType> permissions = permissionService.getPermissions(memberRole);
        if (permissions.contains(PermissionType.EDIT_ROLES) || settlement.isOwner(player)) {
            lore.add("");
            lore.add(Menu.SETTLEMENT_ROLE_EDIT_CLICK.parse());
        }

        return ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_ROLE_EDIT_TITLE.parameterize(index, role.getName(), roleDefault))
                .setMaterial(material)
                .setEnchanted(isDefault)
                .setAmount(index)
                .setLore(lore)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, Role role, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if (!memberService.hasSettlement(player)) return;

            Member member = memberService.getMember(player);
            Role memberRole = roleService.getRole(member);
            Settlement settlement = settlementService.getSettlement(member);
            Set<PermissionType> permissions = permissionService.getPermissions(memberRole);
            if (!(permissions.contains(PermissionType.EDIT_ROLES) || settlement.isOwner(player))) return;

            inventoryService.getRoleInventory(plugin, role).open(player);
        }
    }
}