package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.types.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class RolesInventory extends InventoryPageProvider<Role> {

    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final SettlementService settlementService;
    private final RoleService roleService;

    public RolesInventory(HuskyPlugin plugin, int rows, Role[] roles) {
        super(rows, roles);
        this.plugin = plugin;

        memberService = plugin.provide(MemberService.class);
        inventoryService = plugin.provide(InventoryService.class);
        settlementService = plugin.provide(SettlementService.class);
        roleService = plugin.provide(RoleService.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        contents.set(0, 0, InventoryItem.back(player, inventoryService.getMainInventory(plugin, player)));
    }

    @Override
    public ItemStack construct(Player player, int index, Role role) {
        if (!memberService.hasSettlement(player)) {
            player.closeInventory();
            return null;
        }

        Member member = memberService.getCitizen(player);
        Role memberRole = roleService.getRole(member);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        boolean isDefault = settlement.getDefaultRole().equalsIgnoreCase(role.getName());

        Material material = isDefault ? Material.BOOK : Material.PAPER;
        String defaultRole = isDefault ? Menu.SETTLEMENT_ROLE_EDIT_DEFAULT.parse() : "";

        List<String> lore = Menu.SETTLEMENT_ROLE_EDIT_LORE.parameterizeList();
        if (memberRole.hasPermission(RolePermission.EDIT_ROLES) || settlement.isOwner(player)) {
            lore.add("");
            lore.add(Menu.SETTLEMENT_ROLE_EDIT_CLICK.parse());
        }

        return ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_ROLE_EDIT_TITLE.parameterize(index, role.getName(), defaultRole))
                .setMaterial(material)
                .setEnchanted(isDefault)
                .setAmount(index)
                .setLore(lore)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, Role role, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if (!memberService.hasSettlement(player)) return;

            Member member = memberService.getCitizen(player);
            Role memberRole = roleService.getRole(member);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            if (!(memberRole.hasPermission(RolePermission.EDIT_ROLES) || settlement.isOwner(player))) return;

            inventoryService.getRoleInventory(plugin, player, role).open(player);
        }
    }
}