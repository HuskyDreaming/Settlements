package com.huskydreaming.settlements.inventories.modules.general;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Message;
import com.huskydreaming.settlements.enumeration.locale.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class SpawnModule implements InventoryModule {
    private final ConfigService configService;
    private final MemberService memberService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public SpawnModule(HuskyPlugin plugin) {
        this.configService = plugin.provide(ConfigService.class);
        this.memberService = plugin.provide(MemberService.class);
        this.permissionService = plugin.provide(PermissionService.class);
        this.roleService = plugin.provide(RoleService.class);
        this.settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public ItemStack itemStack(Player player) {
        if (!memberService.hasSettlement(player)) {
            player.closeInventory();
            return null;
        }

        Member member = memberService.getMember(player);
        Role role = roleService.getRole(member);
        Settlement settlement = settlementService.getSettlement(member);

        Set<PermissionType> permissions = permissionService.getPermissions(role);
        boolean permission = permissions.contains(PermissionType.EDIT_SPAWN) || settlement.isOwner(player);

        Menu lore = permission ? Menu.SETTLEMENT_SPAWN_LORE_SET : Menu.SETTLEMENT_SPAWN_LORE;
        return ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_SPAWN_TITLE.parse())
                .setLore(lore.parseList())
                .setMaterial(Material.ENDER_PEARL)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if (!memberService.hasSettlement(player)) return;

            Member member = memberService.getMember(player);
            Role role = roleService.getRole(member);
            Settlement settlement = settlementService.getSettlement(member);

            Set<PermissionType> permissions = permissionService.getPermissions(role);
            if (!(permissions.contains(PermissionType.EDIT_SPAWN) || settlement.isOwner(player))) {
                player.teleport(settlement.toLocation());
                player.sendMessage(Message.GENERAL_TELEPORT.prefix());
                return;
            }

            if (event.isRightClick()) {
                settlement.setLocation(player.getLocation());
                player.closeInventory();
                player.sendMessage(Message.SPAWN_SET.prefix());
                return;
            }

            if (event.isRightClick()) {
                player.teleport(settlement.toLocation());
                player.sendMessage(Message.GENERAL_TELEPORT.prefix());
            }
        }
    }

    @Override
    public boolean isValid(Player player) {
        return configService.getConfig().isTeleportation();
    }
}