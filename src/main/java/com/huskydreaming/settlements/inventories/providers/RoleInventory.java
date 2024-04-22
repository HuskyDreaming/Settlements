package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.types.Message;
import com.huskydreaming.settlements.storage.types.Menu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RoleInventory extends InventoryPageProvider<RolePermission> {

    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final SettlementService settlementService;
    private final RoleService roleService;
    private final Role role;

    public RoleInventory(HuskyPlugin plugin, int rows, Role role, RolePermission[] permissions) {
        super(rows, permissions);
        this.plugin = plugin;

        inventoryService = plugin.provide(InventoryService.class);
        memberService = plugin.provide(MemberService.class);
        settlementService = plugin.provide(SettlementService.class);
        roleService = plugin.provide(RoleService.class);

        this.role = role;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());

        contents.set(0, 0, InventoryItem.back(player, inventoryService.getRolesInventory(plugin, player)));
        contents.set(0, 1, defaultItem(player, settlement));
        contents.set(0, 2, deleteItem(player, settlement, member.getSettlement()));
    }

    @Override
    public ItemStack construct(Player player, int index, RolePermission rolePermission) {
        return InventoryItem.of(role.hasPermission(rolePermission), rolePermission.toString(), rolePermission.getDescription());
    }

    @Override
    public void run(InventoryClickEvent event, RolePermission rolePermission, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if(!memberService.hasSettlement(player)) return;

            if (role.hasPermission(rolePermission)) {
                role.remove(rolePermission);
            } else {
                role.add(rolePermission);
            }
            contents.inventory().open(player);
        }
    }

    private ClickableItem deleteItem(Player player, Settlement settlement, String name) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.ROLE_DELETE_TITLE.parse())
                .setLore(Menu.ROLE_DELETE_LORE.parseList())
                .setMaterial(Material.TNT_MINECART)
                .build(), e -> {
            if (roleService.getRoles(name).size() > 1) {
                if (settlement.getDefaultRole().equalsIgnoreCase(role.getName())) {
                    Role defaultRole = roleService.getOtherRole(name, role.getName());
                    settlement.setDefaultRole(defaultRole.getName());
                }

                memberService.sync(name, settlement.getDefaultRole(), role);
                roleService.remove(name, role);
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
            settlement.setDefaultRole(role.getName());
            inventoryService.getRolesInventory(plugin, player).open(player);
        });
    }
}