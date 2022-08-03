package com.huskydreaming.settlements.inventories.providers;

import com.google.inject.Inject;
import com.huskydreaming.settlements.inventories.InventoryItem;
import com.huskydreaming.settlements.persistence.Citizen;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.CitizenService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CitizenInventory implements InventoryProvider {

    @Inject
    private CitizenService citizenService;

    private final Settlement settlement;
    private final OfflinePlayer offlinePlayer;
    private int index = 0;

    public CitizenInventory(Settlement settlement, OfflinePlayer offlinePlayer) {
        this.settlement = settlement;
        this.offlinePlayer = offlinePlayer;
    }


    @Override
    public void init(Player player, InventoryContents contents) {

        contents.fillBorders(InventoryItem.border());
       // contents.set(0, 0, InventoryItem.back(player, inventoryService.getCitizensInventory(settlement)));
        contents.set(1, 3, setOwner(player, contents));
        contents.set(1, 4, roleItem(player, contents));
        contents.set(1, 5, kickItem(player, contents));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ClickableItem setOwner(Player player, InventoryContents contents) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(ChatColor.GREEN + "Set Owner")
                .setLore(ChatColor.GRAY + "Click to set owner.")
                .setMaterial(Material.EMERALD)
                .build(), e-> {
            if(settlement.isOwner(player)) {

                if(offlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                    player.sendMessage("You already owner of the settlement");
                } else {
                    Player target = offlinePlayer.getPlayer();
                    if (target != null) {
                        target.sendMessage("You have become owner of your settlement.");
                    }
                    player.sendMessage("You have transferred ownership to " + offlinePlayer.getName());
                    settlement.setOwner(offlinePlayer);
                }
            } else {
                player.sendMessage("You must be the settlement owner to transfer ownership.");
            }
            contents.inventory().close(player);
        });
    }

    private ClickableItem roleItem(Player player, InventoryContents contents) {
        Citizen citizen = citizenService.getCitizen(offlinePlayer);
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(ChatColor.GREEN + "Set Role")
                .setLore(ChatColor.GRAY + "Click to set role: " + citizen.getRole())
                .setMaterial(Material.WRITABLE_BOOK)
                .build(), e-> {
            List<Role> roles = new ArrayList<>(settlement.getRoles());
            int size = roles.size();

            index += 1;
            if (index >= size) index = 0;

            citizen.setRole(roles.get(index).getName());
            contents.inventory().open(player);
        });
    }

    private ClickableItem kickItem(Player player, InventoryContents contents) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(ChatColor.YELLOW + "Kick")
                .setLore(ChatColor.GRAY + "Remove from settlement.")
                .setMaterial(Material.ANVIL)
                .build(), e-> {


            Citizen citizen = citizenService.getCitizen(offlinePlayer);
            Role role = settlement.getRole(citizen.getRole());
            if(settlement.isOwner(offlinePlayer) || role.hasPermission(RolePermission.CITIZEN_KICK_EXEMPT)) {
                player.sendMessage("This player can't be kicked from the settlement");
            } else {
                player.sendMessage("You have kicked " + offlinePlayer.getName() + " from the settlement.");
                Player target = offlinePlayer.getPlayer();
                if(target != null) {
                    target.sendMessage("You have been kicked from the " + settlement.getName() + " settlement.");
                }
                citizenService.remove(offlinePlayer);
            }
            contents.inventory().close(player);
        });
    }
}
