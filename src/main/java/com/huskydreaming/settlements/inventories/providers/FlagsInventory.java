package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.enumeration.FlagType;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.*;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class FlagsInventory extends InventoryPageProvider<FlagType> {

    private final HuskyPlugin plugin;
    private final FlagService flagService;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public FlagsInventory(HuskyPlugin plugin, int rows, FlagType[] flagTypes) {
        super(rows, flagTypes);
        this.plugin = plugin;

        flagService = plugin.provide(FlagService.class);
        inventoryService = plugin.provide(InventoryService.class);
        memberService = plugin.provide(MemberService.class);
        permissionService = plugin.provide(PermissionService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        contents.set(0, 0, InventoryItem.back(player, inventoryService.getMainInventory(plugin, player)));
    }

    @Override
    public ItemStack construct(Player player, int i, FlagType flagType) {
        if (!memberService.hasSettlement(player)) {
            player.closeInventory();
            return null;
        }

        Member member = memberService.getMember(player);
        Role role = roleService.getRole(member);
        Settlement settlement = settlementService.getSettlement(member);

        boolean enabled = flagService.hasFlag(settlement, flagType);
        Set<PermissionType> permissions = permissionService.getPermissions(role);
        if (!(permissions.contains(PermissionType.EDIT_FLAGS) || settlement.isOwner(player))) {
            return InventoryItem.of(enabled, false, flagType.toString(), flagType.getDescription());
        }

        return InventoryItem.of(enabled, flagType.toString(), flagType.getDescription());
    }

    @Override
    public void run(InventoryClickEvent event, FlagType flagType, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if (!memberService.hasSettlement(player)) return;

            Member member = memberService.getMember(player);
            Role role = roleService.getRole(member);
            Settlement settlement = settlementService.getSettlement(member);

            Set<PermissionType> permissions = permissionService.getPermissions(role);
            if (!(permissions.contains(PermissionType.EDIT_FLAGS) || settlement.isOwner(player))) return;

            boolean enabled = flagService.hasFlag(settlement, flagType);
            if (enabled) {
                flagService.removeFlag(settlement, flagType, () -> contents.inventory().open(player));
            } else {
                flagService.addFlag(settlement, flagType, () -> contents.inventory().open(player));
            }
        }
    }
}