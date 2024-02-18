package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Menu;
import com.huskydreaming.settlements.utilities.Remote;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RoleInventory extends InventoryPageProvider<RolePermission> {

    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final Settlement settlement;
    private final Role role;

    public RoleInventory(Settlement settlement, int rows, Role role) {
        super(settlement, rows, RolePermission.values());
        inventoryService = ServiceProvider.Provide(InventoryService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        roleService = ServiceProvider.Provide(RoleService.class);

        this.smartInventory = inventoryService.getRolesInventory(settlement);
        this.settlement = settlement;
        this.role = role;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);
        contents.set(0, 1, defaultItem(player));
        contents.set(0, 2, deleteItem(player));
    }

    @Override
    public ItemStack construct(int index, RolePermission rolePermission) {
        boolean enabled = this.role.hasPermission(rolePermission);

        String materialEnabled = Menu.ROLE_SETTING_ENABLE_MATERIAL.parse();
        String materialDisabled = Menu.ROLE_SETTING_DISABLE_MATERIAL.parse();

        String colorEnabled = Menu.ROLE_SETTING_ENABLE_COLOR.parse();
        String colorDisabled = Menu.ROLE_SETTING_DISABLE_COLOR.parse();

        return ItemBuilder.create()
                .setDisplayName(ChatColor.valueOf(enabled ? colorEnabled : colorDisabled) + rolePermission.getName())
                .setLore(enabled ? Menu.ROLE_SETTING_ENABLE_LORE.parse() : Menu.ROLE_SETTING_DISABLE_LORE.parse())
                .setMaterial(Material.valueOf(enabled ? materialEnabled : materialDisabled))
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, RolePermission rolePermission, InventoryContents contents) {
        if(event.getWhoClicked() instanceof Player) {
            Player player = ((Player) event.getWhoClicked()).getPlayer();
            if (role.hasPermission(rolePermission)) {
                role.remove(rolePermission);
            } else {
                role.add(rolePermission);
            }
            contents.inventory().open(player);
        }
    }

    private ClickableItem deleteItem(Player player) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.ROLE_DELETE_TITLE.parse())
                .setLore(Menu.ROLE_DELETE_LORE.parseList())
                .setMaterial(Material.TNT_MINECART)
                .build(), e -> {
            if(roleService.getRoles(settlement).size() > 1) {
                if(settlement.getDefaultRole().equalsIgnoreCase(role.getName())) {
                    Role defaultRole = roleService.getOtherRole(settlement, role.getName());
                    settlement.setDefaultRole(defaultRole.getName());
                }

                memberService.sync(settlement, role);
                roleService.remove(settlement, role);
                inventoryService.getRolesInventory(settlement).open(player);
            } else {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_ROLE_ONE));
            }
        });
    }

    private ClickableItem defaultItem(Player player) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.ROLE_DEFAULT_TITLE.parse())
                .setLore(Menu.ROLE_DEFAULT_LORE.parseList())
                .setMaterial(Material.DIAMOND)
                .build(), e-> {
            settlement.setDefaultRole(role.getName());
            inventoryService.getRolesInventory(settlement).open(player);
        });
    }
}
