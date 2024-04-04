package com.huskydreaming.settlements.inventories.modules.admin;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.Permission;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.enumeration.NotificationType;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.storage.types.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AdminNotificationModule implements InventoryModule {

    private final ConfigService configService;

    public AdminNotificationModule(HuskyPlugin plugin) {
        configService = plugin.provide(ConfigService.class);
    }

    @Override
    public ItemStack itemStack(Player player) {
        Config config = configService.getConfig();
        NotificationType notificationType = config.getNotificationType();

        Material material = switch (notificationType) {
            case ACTION_BAR -> Material.BOOK;
            case BOSS_BAR -> Material.WRITABLE_BOOK;
            case TITLE -> Material.PAPER;
        };

        return ItemBuilder.create()
                .setDisplayName(Menu.ADMIN_NOTIFICATION_TITLE.parse())
                .setLore(Menu.ADMIN_NOTIFICATION_LORE.parameterizeList(notificationType.toString()))
                .setMaterial(material)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            Config config = configService.getConfig();
            NotificationType notificationType = config.getNotificationType();
            configService.selectNotificationType(notificationType);
            contents.inventory().open(player);
        }
    }

    @Override
    public Permission getPermission() {
        return RolePermission.DEFAULT;
    }
}