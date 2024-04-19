package com.huskydreaming.settlements.inventories.modules.general;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.types.Locale;
import com.huskydreaming.settlements.storage.types.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SpawnModule implements InventoryModule {
    private final ConfigService configService;

    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public SpawnModule(HuskyPlugin plugin) {
        this.configService = plugin.provide(ConfigService.class);
        this.memberService = plugin.provide(MemberService.class);
        this.roleService = plugin.provide(RoleService.class);
        this.settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public ItemStack itemStack(Player player) {
        if (!memberService.hasSettlement(player)) {
            player.closeInventory();
            return null;
        }

        Member member = memberService.getCitizen(player);
        Role role = roleService.getRole(member);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());

        boolean permission = role.hasPermission(RolePermission.EDIT_SPAWN) || settlement.isOwner(player);

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

            Member member = memberService.getCitizen(player);
            Role role = roleService.getRole(member);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());

            if (!(role.hasPermission(RolePermission.EDIT_SPAWN) || settlement.isOwner(player))) {
                player.teleport(settlement.getLocation());
                player.sendMessage(Locale.SETTLEMENT_TELEPORT.prefix());
                return;
            }

            if (event.isRightClick()) {
                settlement.setLocation(player.getLocation());
                player.closeInventory();
                player.sendMessage(Locale.SPAWN_SET.prefix());
                return;
            }

            if (event.isRightClick()) {
                player.teleport(settlement.getLocation());
                player.sendMessage(Locale.SETTLEMENT_TELEPORT.prefix());
            }
        }
    }

    @Override
    public boolean isValid(Player player) {
        return configService.getConfig().isTeleportation();
    }
}