package com.huskydreaming.settlements.inventories.providers;

import com.google.inject.Inject;
import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.InventoryService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RoleInventory extends InventoryPageProvider<RolePermission> {

    @Inject
    private  InventoryService inventoryService;
    private final Settlement settlement;
    private final Role role;

    public RoleInventory(Settlement settlement, int rows, Role role) {
        super(settlement, rows, RolePermission.values());

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

        ChatColor color = enabled ? ChatColor.GREEN : ChatColor.RED;
        String lore = enabled ? "Click to disable." : "Click to enable";
        Material material = enabled ? Material.LIME_DYE : Material.GRAY_DYE;

        return ItemBuilder.create()
                .setDisplayName(color + rolePermission.getName())
                .setLore(ChatColor.GRAY + lore)
                .setMaterial(material)
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
                .setDisplayName(ChatColor.RED + "Delete")
                .setLore(ChatColor.GRAY + "Click to delete role.")
                .setMaterial(Material.TNT_MINECART)
                .build(), e -> {
            settlement.remove(role);
            inventoryService.getRolesInventory(settlement).open(player);
        });
    }

    private ClickableItem defaultItem(Player player) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(ChatColor.AQUA + "Default")
                .setLore(ChatColor.GRAY + "Set role as default.")
                .setMaterial(Material.DIAMOND)
                .build(), e-> {
            settlement.setDefaultRole(role.getName());
            inventoryService.getRolesInventory(settlement).open(player);
        });
    }
}
