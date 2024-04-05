package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.settlements.enumeration.filters.MemberFilter;
import com.huskydreaming.settlements.inventories.base.InventoryAction;
import com.huskydreaming.settlements.inventories.base.InventoryActionType;
import com.huskydreaming.settlements.storage.persistence.Role;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface InventoryService extends Service {

    void removeModule(Class<?> moduleClass);

    void addModule(InventoryModule module);

    void addAction(Player player, InventoryAction action);

    void acceptAction(Player player);

    void denyAction(Player player);

    InventoryActionType getActionType(Player player);

    SmartInventory getAdminInventory(HuskyPlugin plugin);

    SmartInventory getWorldsInventory(HuskyPlugin plugin);

    SmartInventory getRoleInventory(HuskyPlugin plugin, Player player, Role role);

    SmartInventory getMainInventory(HuskyPlugin plugin, Player player);

    SmartInventory getSettlementsInventory(HuskyPlugin plugin);

    SmartInventory getConfirmationInventory(HuskyPlugin plugin, Player player);

    SmartInventory getRolesInventory(HuskyPlugin plugin, Player player);

    SmartInventory getFlagsInventory(HuskyPlugin plugin, Player player);

    SmartInventory getClaimsInventory(HuskyPlugin plugin, Player player);

    SmartInventory getMembersInventory(HuskyPlugin plugin, Player player, MemberFilter memberFilter);

    SmartInventory getMemberInventory(HuskyPlugin plugin, OfflinePlayer offlinePlayer);
}