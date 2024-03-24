package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.inventories.InventoryAction;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.Locale;
import com.huskydreaming.settlements.storage.Menu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ConfirmationInventory implements InventoryProvider {

    private final HuskyPlugin plugin;
    private final BorderService borderService;
    private final FlagService flagService;
    private final MemberService memberService;
    private final ChunkService chunkService;
    private final InventoryService inventoryService;
    private final RoleService roleService;
    private final SettlementService settlementService;
    private final InventoryAction inventoryAction;
    private final String name;

    public ConfirmationInventory(HuskyPlugin plugin, String name, InventoryAction inventoryAction) {
        this.plugin = plugin;

        borderService = plugin.provide(BorderService.class);
        flagService = plugin.provide(FlagService.class);
        settlementService = plugin.provide(SettlementService.class);
        chunkService = plugin.provide(ChunkService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        inventoryService = plugin.provide(InventoryService.class);

        this.name = name;
        this.inventoryAction = inventoryAction;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(InventoryItem.border());

        contents.set(1, 3, accept(player));
        contents.set(1, 5, deny(player, name));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ClickableItem accept(Player player) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.CONFIRMATION_YES_TITLE.parse())
                .setMaterial(Material.GREEN_TERRACOTTA)
                .build();

        return ClickableItem.of(itemStack, e -> runAction(player));
    }

    private ClickableItem deny(Player player, String name) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.CONFIRMATION_NO_TITLE.parse())
                .setMaterial(Material.RED_TERRACOTTA)
                .build();

        return ClickableItem.of(itemStack, e -> inventoryService.getSettlementInventory(plugin, name).open(player));
    }

    public void runAction(Player player) {
        if (Objects.requireNonNull(inventoryAction) == InventoryAction.DISBAND) {
            chunkService.clean(name);
            memberService.clean(name);
            flagService.clean(name);
            roleService.clean(name);
            settlementService.disbandSettlement(name);
            borderService.removePlayer(player);

            player.closeInventory();
            player.sendMessage(Locale.SETTLEMENT_DISBAND.prefix());
        }
    }
}