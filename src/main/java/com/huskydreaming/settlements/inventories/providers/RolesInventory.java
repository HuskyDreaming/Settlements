package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class RolesInventory extends InventoryPageProvider<Role> {

    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;
    private final SettlementService settlementService;
    private final RoleService roleService;
    private final String settlementName;

    public RolesInventory(HuskyPlugin plugin, String settlementName, int rows) {
        super(rows, null);
        this.plugin = plugin;

        inventoryService = plugin.provide(InventoryService.class);
        settlementService = plugin.provide(SettlementService.class);
        roleService = plugin.provide(RoleService.class);

        this.settlementName = settlementName;
        this.array = roleService.getRoles(settlementName).toArray(new Role[0]);
        this.smartInventory = inventoryService.getSettlementInventory(plugin, settlementName);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);
    }

    @Override
    public ItemStack construct(Player player, int index, Role role) {
        Settlement settlement = settlementService.getSettlement(settlementName);
        boolean isDefault = settlement.getDefaultRole().equalsIgnoreCase(role.getName());

        Material material = isDefault ? Material.BOOK : Material.PAPER;
        String defaultRole = isDefault ? Menu.SETTLEMENT_ROLE_EDIT_DEFAULT.parse() : "";

        return ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_ROLE_EDIT_TITLE.parameterize(index, role.getName(), defaultRole))
                .setLore(Menu.SETTLEMENT_ROLE_EDIT_LORE.parseList())
                .setMaterial(material)
                .setEnchanted(isDefault)
                .setAmount(index)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, Role role, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if (event.isLeftClick()) {
                inventoryService.getRoleInventory(plugin, settlementName, role).open(player);
            } else if (event.isRightClick()) {
                List<Role> roles = roleService.getRoles(settlementName);
                int index = roleService.getIndex(settlementName, role.getName());
                if (index < roles.size() - 1) Collections.swap(roles, index, index + 1);
                contents.inventory().open(player);
            }
        }
    }
}