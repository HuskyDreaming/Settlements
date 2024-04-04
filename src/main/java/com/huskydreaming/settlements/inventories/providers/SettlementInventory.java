package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.inventories.actions.DisbandInventoryAction;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.types.Menu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class SettlementInventory extends InventoryPageProvider<InventoryModule> {

    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public SettlementInventory(HuskyPlugin plugin, int rows) {
        super(rows);
        this.plugin = plugin;

        this.inventoryService = plugin.provide(InventoryService.class);
        this.memberService = plugin.provide(MemberService.class);
        this.roleService = plugin.provide(RoleService.class);
        this.settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_DISBAND_TITLE.parse())
                .setLore(Menu.SETTLEMENT_DISBAND_LORE.parseList())
                .setMaterial(Material.TNT_MINECART)
                .build();


        Consumer<InventoryClickEvent> eventConsumer = e -> {
            if (memberService.hasSettlement(player)) {
                Member member = memberService.getCitizen(player);
                inventoryService.addAction(player, new DisbandInventoryAction(plugin, member.getSettlement()));
                inventoryService.getConfirmationInventory(plugin, player).open(player);
            } else {
                player.closeInventory();
            }
        };

        contents.set(Math.min(rows + 2, 5) - 1, 8, ClickableItem.of(itemStack, eventConsumer));
    }

    @Override
    public ItemStack construct(Player player, int i, InventoryModule module) {
        Member member = memberService.getCitizen(player);
        Role role = roleService.getRole(member);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());

        RolePermission rolePermission = (RolePermission) module.getPermission();
        boolean permission = role.hasPermission(rolePermission) || settlement.isOwner(player);
        return InventoryItem.of(permission, module.itemStack(player));
    }

    @Override
    public void run(InventoryClickEvent event, InventoryModule module, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            Member member = memberService.getCitizen(player);
            Role role = roleService.getRole(member);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());

            RolePermission rolePermission = (RolePermission) module.getPermission();
            boolean permissions = role.hasPermission(rolePermission) || settlement.isOwner(player);
            if (permissions || module.getPermission() == RolePermission.DEFAULT) module.run(event, contents);
        }
    }
}