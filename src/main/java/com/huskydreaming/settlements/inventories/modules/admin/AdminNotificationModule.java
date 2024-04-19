package com.huskydreaming.settlements.inventories.modules.admin;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.enumeration.types.NotificationType;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.storage.types.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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
            case NONE -> Material.BARRIER;
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
            List<NotificationType> notificationTypes = List.of(NotificationType.values());

            int index = notificationTypes.indexOf(notificationType);
            if (index < notificationTypes.size() - 1) {
                index += 1;
            } else {
                index = 0;
            }

            config.setNotificationType(notificationTypes.get(index));
            contents.inventory().open(player);
        }
    }
}