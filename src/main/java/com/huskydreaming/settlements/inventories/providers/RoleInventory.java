package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.InventoryService;
import com.huskydreaming.settlements.services.base.ServiceRegistry;
import com.huskydreaming.settlements.services.base.ServiceType;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoleInventory extends InventoryPageProvider<RolePermission> {

    private final InventoryService inventoryService;
    private int index = 0;
    private final Settlement settlement;
    private final Role role;

    public RoleInventory(Settlement settlement, int rows, Role role) {
        super(settlement, rows, RolePermission.values());
        inventoryService = (InventoryService) ServiceRegistry.getService(ServiceType.INVENTORY);

        this.smartInventory = inventoryService.getRolesInventory(settlement);
        this.settlement = settlement;
        this.role = role;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);
        contents.set(0, 1, defaultItem(player));
        contents.set(0, 2, parentItem(player, contents));
        contents.set(0, 3, deleteItem(player));
    }

    @Override
    public ItemStack construct(int index, RolePermission rolePermission) {
        Role role = settlement.getRole(this.role.getParent());

        boolean enabled = this.role.hasPermission(rolePermission);
        boolean parentEnabled = role != null && role.hasPermission(rolePermission);

        ChatColor color = enabled ? ChatColor.GREEN : ChatColor.RED;
        ChatColor colorParent = parentEnabled ? ChatColor.AQUA : color;

        String displayName = role != null ? role.getName() : "null";
        String lore = enabled ? "Click to disable." : "Click to enable";
        String loreParent = parentEnabled ? "Parent Inheritance: " + displayName : lore;

        Material material = enabled ? Material.LIME_DYE : Material.GRAY_DYE;
        Material materialParent = parentEnabled ? Material.LIGHT_BLUE_DYE : material;

        return ItemBuilder.create()
                .setDisplayName(colorParent + rolePermission.getName())
                .setLore(ChatColor.GRAY + loreParent)
                .setMaterial(materialParent)
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

    private ClickableItem parentItem(Player player, InventoryContents contents) {
        List<String> child = new ArrayList<>();
        boolean isChild = child.isEmpty();

        String lore = isChild ? "This role is a parent for " + child : "Click to set parent: " + role.getParent();
        Material material = isChild ? Material.BOOK : Material.WRITABLE_BOOK;

        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(ChatColor.GREEN + "Parent")
                .setLore(ChatColor.GRAY + lore)
                .setMaterial(material)
                .build();

        ClickableItem clickableItem = ClickableItem.empty(itemStack);
        if(!isChild) {
            List<String> roles = settlement.getRoles().stream().map(Role::getName).collect(Collectors.toList());
            roles.remove(role.getName());
            roles.add("none");

            clickableItem = ClickableItem.of(itemStack, e -> {
                index += 1;
                if (index >= roles.size()) index = 0;
                role.setParent(roles.get(index));
                contents.inventory().open(player);
            });
        }

        return clickableItem;
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
