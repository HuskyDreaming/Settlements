package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.storage.parseables.DefaultMenu;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.Locale;
import com.huskydreaming.settlements.storage.Menu;
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

    private final String settlementName;

    public RoleInventory(HuskyPlugin plugin, String settlementName, int rows, Role role) {
        super(rows, RolePermission.values());
        this.plugin = plugin;

        inventoryService = plugin.provide(InventoryService.class);
        memberService = plugin.provide(MemberService.class);
        settlementService = plugin.provide(SettlementService.class);
        roleService = plugin.provide(RoleService.class);

        this.role = role;
        this.settlementName = settlementName;
        this.smartInventory = inventoryService.getRolesInventory(plugin, settlementName);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());

        contents.set(0, 1, defaultItem(player, settlement));
        contents.set(0, 2, deleteItem(player, settlement));
    }

    @Override
    public ItemStack construct(Player player, int index, RolePermission rolePermission) {
        boolean enabled = this.role.hasPermission(rolePermission);

        String materialEnabled = DefaultMenu.ENABLE_MATERIAL.parse();
        String materialDisabled = DefaultMenu.DISABLED_MATERIAL.parse();

        String displayNameEnabled = DefaultMenu.ENABLE_TITLE.parameterize(rolePermission.getName());
        String displayNameDisabled = DefaultMenu.DISABLED_TITLE.parameterize(rolePermission.getName());

        return ItemBuilder.create()
                .setDisplayName(enabled ? displayNameEnabled : displayNameDisabled)
                .setLore(enabled ? DefaultMenu.DISABLED_DESCRIPTION.parse() : DefaultMenu.ENABLED_DESCRIPTION.parse())
                .setMaterial(Material.valueOf(enabled ? materialEnabled : materialDisabled))
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, RolePermission rolePermission, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = ((Player) event.getWhoClicked()).getPlayer();
            if (role.hasPermission(rolePermission)) {
                role.remove(rolePermission);
            } else {
                role.add(rolePermission);
            }
            contents.inventory().open(player);
        }
    }

    private ClickableItem deleteItem(Player player, Settlement settlement) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.ROLE_DELETE_TITLE.parse())
                .setLore(Menu.ROLE_DELETE_LORE.parseList())
                .setMaterial(Material.TNT_MINECART)
                .build(), e -> {
            if (roleService.getRoles(settlementName).size() > 1) {
                if (settlement.getDefaultRole().equalsIgnoreCase(role.getName())) {
                    Role defaultRole = roleService.getOtherRole(settlementName, role.getName());
                    settlement.setDefaultRole(defaultRole.getName());
                }

                memberService.sync(settlementName, settlement.getDefaultRole(), role);
                roleService.remove(settlementName, role);
                inventoryService.getRolesInventory(plugin, settlementName).open(player);
            } else {
                player.sendMessage(Locale.SETTLEMENT_ROLE_ONE.prefix());
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
            inventoryService.getRolesInventory(plugin, settlementName).open(player);
        });
    }
}