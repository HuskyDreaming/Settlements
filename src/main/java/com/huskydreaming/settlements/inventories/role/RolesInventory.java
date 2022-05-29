package com.huskydreaming.settlements.inventories.role;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.inventories.InventorySupplier;
import com.huskydreaming.settlements.persistence.Request;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RolesInventory extends InventoryPageProvider<Role> {

    public RolesInventory(Settlement settlement, int rows) {
        super(settlement, rows, settlement.getRoles().toArray(new Role[0]));
        this.smartInventory = InventorySupplier.getSettlementInventory(settlement);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        contents.set(0, 1, createItem(player, contents));
    }

    @Override
    public ItemStack construct(int index, Role role) {
        boolean isDefault = role.getName().equalsIgnoreCase(settlement.getDefaultRole());

        Material material = isDefault ? Material.BOOK : Material.PAPER;
        String defaultRole = isDefault ? "(Default)" : "";
        String defaultName = ChatColor.GRAY + defaultRole;
        String displayName = ChatColor.GREEN + "" + index + ". " + role.getName();

        return ItemBuilder.create()
                .setDisplayName(displayName + " " + defaultName)
                .setLore(ChatColor.GRAY + "Click to edit role.")
                .setMaterial(material)
                .setEnchanted(isDefault)
                .setAmount(index)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, Role role, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            InventorySupplier.getRoleInventory(settlement, role).open(player);
        }
    }

    private ClickableItem createItem(Player player, InventoryContents contents) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(ChatColor.GREEN + "Create")
                .setLore(ChatColor.GRAY + "Create new role.")
                .setMaterial(Material.WRITABLE_BOOK)
                .build(), e-> {
            contents.inventory().close(player);
            Settlements.getInstance().getRequestManager().create(player, Request.Type.ROLE_CREATE);
        });
    }
}
