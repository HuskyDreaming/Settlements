package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.enumeration.Flag;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class FlagsInventory extends InventoryPageProvider<Flag> {

    private final HuskyPlugin plugin;
    private final FlagService flagService;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public FlagsInventory(HuskyPlugin plugin, int rows, Flag[] flags) {
        super(rows, flags);
        this.plugin = plugin;

        flagService = plugin.provide(FlagService.class);
        inventoryService = plugin.provide(InventoryService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        contents.set(0, 0, InventoryItem.back(player, inventoryService.getMainInventory(plugin, player)));
    }

    @Override
    public ItemStack construct(Player player, int i, Flag flag) {
        if (!memberService.hasSettlement(player)) {
            player.closeInventory();
            return null;
        }

        Member member = memberService.getCitizen(player);
        Role role = roleService.getRole(member);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());

        boolean enabled = flagService.hasFlag(member.getSettlement(), flag);
        if (!(role.hasPermission(RolePermission.EDIT_FLAGS) || settlement.isOwner(player))) {
            return InventoryItem.of(enabled, false, flag.toString(), flag.getDescription());
        }

        return InventoryItem.of(enabled, flag.toString(), flag.getDescription());
    }

    @Override
    public void run(InventoryClickEvent event, Flag flag, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if (!memberService.hasSettlement(player)) return;

            Member member = memberService.getCitizen(player);
            Role role = roleService.getRole(member);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());

            if (!(role.hasPermission(RolePermission.EDIT_FLAGS) || settlement.isOwner(player))) return;

            boolean enabled = flagService.hasFlag(member.getSettlement(), flag);
            if (enabled) {
                flagService.removeFlag(member.getSettlement(), flag);
            } else {
                flagService.addFlag(member.getSettlement(), flag);
            }
            contents.inventory().open(player);
        }
    }
}