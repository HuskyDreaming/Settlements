package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.settlements.inventories.InventoryAction;
import com.huskydreaming.settlements.inventories.InventoryItem;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.storage.enumerations.Menu;
import com.huskydreaming.settlements.utilities.Remote;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ConfirmationInventory implements InventoryProvider {

    private final BorderService borderService;

    private final MemberService memberService;
    private final ClaimService claimService;
    private final InventoryService inventoryService;

    private final RoleService roleService;

    private final SettlementService settlementService;

    private final Settlement settlement;
    private final InventoryAction inventoryAction;

    public ConfirmationInventory(Settlement settlement, InventoryAction inventoryAction) {

        borderService = ServiceProvider.Provide(BorderService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
        claimService = ServiceProvider.Provide(ClaimService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        roleService = ServiceProvider.Provide(RoleService.class);
        inventoryService = ServiceProvider.Provide(InventoryService.class);

        this.settlement = settlement;
        this.inventoryAction = inventoryAction;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(InventoryItem.border());

        contents.set(1, 3, accept(player));
        contents.set(1, 5, deny(player, settlement));
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

    private ClickableItem deny(Player player, Settlement settlement) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.CONFIRMATION_NO_TITLE.parse())
                .setMaterial(Material.RED_TERRACOTTA)
                .build();

        return ClickableItem.of(itemStack, e -> inventoryService.getSettlementInventory(settlement).open(player));
    }

    public void runAction(Player player) {
        if (Objects.requireNonNull(inventoryAction) == InventoryAction.DISBAND) {
            claimService.clean(settlement);
            memberService.clean(settlement);
            roleService.clean(settlement);
            settlementService.disbandSettlement(settlement);
            borderService.removePlayer(player);

            player.closeInventory();
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_DISBAND));
        }
    }
}
