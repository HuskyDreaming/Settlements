package com.huskydreaming.settlements.inventories.modules.general;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class HomeModule implements InventoryModule {

    private final HuskyPlugin plugin;
    private final ConfigService configService;
    private final HomeService homeService;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final SettlementService settlementService;

    public HomeModule(HuskyPlugin plugin) {
        this.plugin = plugin;

        configService = plugin.provide(ConfigService.class);
        homeService = plugin.provide(HomeService.class);
        inventoryService = plugin.provide(InventoryService.class);
        memberService = plugin.provide(MemberService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public ItemStack itemStack(Player player) {
        return ItemBuilder.create()
                .setDisplayName(Menu.SETTLEMENT_HOMES_TITLE.parse())
                .setLore(Menu.SETTLEMENT_HOMES_LORE.parseList())
                .setMaterial(Material.WHITE_BED)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if (!memberService.hasSettlement(player)) return;
            Member member = memberService.getMember(player);
            Settlement settlement = settlementService.getSettlement(member);

            if (!homeService.hasHomes(settlement)) return;
            inventoryService.getHomesInventory(plugin, player).open(player);
        }
    }

    @Override
    public boolean isValid(Player player) {
        if (!memberService.hasSettlement(player)) return false;
        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);

        return configService.getConfig().isHomes() && homeService.hasHomes(settlement);
    }
}