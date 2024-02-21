package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class RolesInventory extends InventoryPageProvider<Role> {

    private final InventoryService inventoryService;
    private final RoleService roleService;

    public RolesInventory(Settlement settlement, int rows) {
        super(settlement, rows, null);
        inventoryService = ServiceProvider.Provide(InventoryService.class);
        roleService = ServiceProvider.Provide(RoleService.class);

        this.array = roleService.getRoles(settlement).toArray(new Role[0]);
        this.smartInventory = inventoryService.getSettlementInventory(settlement);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);
    }

    @Override
    public ItemStack construct(int index, Role role) {
        boolean isDefault = settlement.getDefaultRole().equalsIgnoreCase(role.getName());

        Material material = isDefault ? Material.BOOK : Material.PAPER;
        String defaultRole = isDefault ? "(Default)" : "";
        String defaultName = ChatColor.GRAY + defaultRole;
        String displayName = ChatColor.GREEN + "" + index + ". " + role.getName();

        return ItemBuilder.create()
                .setDisplayName(displayName + " " + defaultName)
                .setLore(ChatColor.GRAY + "Left-Click to edit role.", ChatColor.GRAY + "Right-click to increase priority.")
                .setMaterial(material)
                .setEnchanted(isDefault)
                .setAmount(index)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, Role role, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if(event.isLeftClick()) {
                inventoryService.getRoleInventory(settlement, role).open(player);
            } else if(event.isRightClick()) {
                List<Role> roles = roleService.getRoles(settlement);
                int index = roleService.getIndex(settlement, role.getName());
                if(index < roles.size() - 1) Collections.swap(roles, index, index + 1);
                contents.inventory().open(player);
            }
        }
    }
}
