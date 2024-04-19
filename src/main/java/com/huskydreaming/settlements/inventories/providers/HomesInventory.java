package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.storage.persistence.Home;
import com.huskydreaming.settlements.storage.types.Locale;
import com.huskydreaming.settlements.storage.types.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class HomesInventory extends InventoryPageProvider<Home> {

    private final HuskyPlugin plugin;
    private final MemberService memberService;
    private final InventoryService inventoryService;

    public HomesInventory(HuskyPlugin plugin, int rows) {
        super(rows);
        this.plugin = plugin;

        memberService = plugin.provide(MemberService.class);
        inventoryService = plugin.provide(InventoryService.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        contents.set(0, 0, InventoryItem.back(player, inventoryService.getMainInventory(plugin, player)));
    }

    @Override
    public ItemStack construct(Player player, int i, Home home) {
        if (!memberService.hasSettlement(player)) {
            player.closeInventory();
            return null;
        }

        Location location = home.location();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return ItemBuilder
                .create()
                .setDisplayName(Menu.SETTLEMENT_HOME_TITLE.parameterize(Util.capitalize(home.name())))
                .setLore(Menu.SETTLEMENT_HOME_LORE.parameterizeList((int) x, (int) y, (int) z, home.name()))
                .setMaterial(home.material())
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, Home home, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if (!memberService.hasSettlement(player)) return;

            player.teleport(home.location());
            player.sendMessage(Locale.HOME_TELEPORT.prefix(Util.capitalize(home.name())));
        }
    }
}