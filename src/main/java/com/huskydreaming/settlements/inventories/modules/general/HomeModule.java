package com.huskydreaming.settlements.inventories.modules.general;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.HomeService;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.types.Menu;
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

    public HomeModule(HuskyPlugin plugin) {
        this.plugin = plugin;

        configService = plugin.provide(ConfigService.class);
        homeService = plugin.provide(HomeService.class);
        inventoryService = plugin.provide(InventoryService.class);
        memberService = plugin.provide(MemberService.class);
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
            Member member = memberService.getCitizen(player);

            if (!homeService.hasHomes(member.getSettlement())) return;
            inventoryService.getHomesInventory(plugin, player).open(player);
        }
    }

    @Override
    public boolean isValid(Player player) {
        return configService.getConfig().isHomes();
    }
}