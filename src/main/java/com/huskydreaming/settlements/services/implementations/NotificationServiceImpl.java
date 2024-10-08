package com.huskydreaming.settlements.services.implementations;

  import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.database.persistence.Config;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.NotificationService;
import com.huskydreaming.settlements.enumeration.locale.Message;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationServiceImpl implements NotificationService {

    private final HuskyPlugin plugin;
    private final ConfigService configService;

    private final Map<UUID, BossBar> bossBarMap = new ConcurrentHashMap<>();

    public NotificationServiceImpl(HuskyPlugin plugin) {
        this.plugin = plugin;
        this.configService = plugin.provide(ConfigService.class);
    }

    @Override
    public void sendTrust(Player player, Settlement settlement) {
        String settlementName = Util.capitalize(settlement.getName());

        Config config = configService.getConfig();
        switch (config.getNotificationType()) {
            case TITLE -> {
                String header = Message.NOTIFICATION_TITLE_HEADER.parameterize(ChatColor.YELLOW, settlementName);
                String footer = Message.NOTIFICATION_TITLE_FOOTER.parameterize(settlement.getDescription());
                sendTitle(player, header, footer);
            }
            case BOSS_BAR -> {
                String message = Message.NOTIFICATION_BOSS_BAR.parameterize(ChatColor.YELLOW, settlementName);
                sendBossBar(player, BarColor.YELLOW, message);
            }
            case ACTION_BAR -> {
                String message = Message.NOTIFICATION_ACTION_BAR.parameterize(ChatColor.YELLOW, settlementName);
                sendActionBar(player, message);
            }
        }
    }

    @Override
    public void sendWilderness(Player player) {
        Config config = configService.getConfig();
        switch (config.getNotificationType()) {
            case TITLE -> {
                String header = Message.NOTIFICATION_WILDERNESS_TITLE_HEADER.parse();
                String footer = Message.NOTIFICATION_WILDERNESS_TITLE_FOOTER.parse();
                sendTitle(player, header, footer);
            }
            case BOSS_BAR -> {
                String title = Message.NOTIFICATION_WILDERNESS_BOSS_BAR.parse();
                sendBossBar(player, BarColor.GREEN, title);
            }
            case ACTION_BAR -> {
                String title = Message.NOTIFICATION_WILDERNESS_ACTION_BAR.parse();
                sendActionBar(player, title);
            }
        }
    }

    @Override
    public void sendSettlement(Player player, Settlement settlement, boolean isClaim) {
        BarColor barColor = isClaim ? BarColor.BLUE : BarColor.RED;
        ChatColor chatColor = isClaim ? ChatColor.AQUA : ChatColor.RED;
        String settlementName = Util.capitalize(settlement.getName());

        Config config = configService.getConfig();
        switch (config.getNotificationType()) {
            case TITLE -> {
                String header = Message.NOTIFICATION_TITLE_HEADER.parameterize(chatColor, settlementName);
                String footer = Message.NOTIFICATION_TITLE_FOOTER.parameterize(settlement.getDescription());
                sendTitle(player, header, footer);
            }
            case BOSS_BAR -> {
                String message = Message.NOTIFICATION_BOSS_BAR.parameterize(chatColor, settlementName);
                sendBossBar(player, barColor, message);
            }
            case ACTION_BAR -> {
                String message = Message.NOTIFICATION_ACTION_BAR.parameterize(chatColor, settlementName);
                sendActionBar(player, message);
            }
        }
    }

    private void sendActionBar(Player player, String string) {
        if (bossBarMap.containsKey(player.getUniqueId())) {
            bossBarMap.get(player.getUniqueId()).removePlayer(player);
            bossBarMap.remove(player.getUniqueId());
        }
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(string));
    }

    private void sendTitle(Player player, String header, String footer) {
        if (bossBarMap.containsKey(player.getUniqueId())) {
            bossBarMap.get(player.getUniqueId()).removePlayer(player);
            bossBarMap.remove(player.getUniqueId());
        }
        player.sendTitle(header, footer, 20, 40, 20);
    }

    private void sendBossBar(Player player, BarColor color, String message) {
        Bukkit.getBossBars().forEachRemaining(keyedBossBar -> player.sendMessage(keyedBossBar.getKey().getKey()));
        if (bossBarMap.containsKey(player.getUniqueId())) {
            bossBarMap.get(player.getUniqueId()).removePlayer(player);
            bossBarMap.remove(player.getUniqueId());
        }

        BossBar bossBar = Bukkit.createBossBar(message, color, BarStyle.SOLID);
        bossBar.addPlayer(player);

        new BukkitRunnable() {
            double countdown = 40.0D;

            @Override
            public void run() {
                if (bossBarMap.containsKey(player.getUniqueId())) {
                    BossBar fromMap = bossBarMap.get(player.getUniqueId());
                    if (fromMap != bossBar) {
                        bossBar.removePlayer(player);
                        cancel();
                        return;
                    }
                }

                if (countdown <= 0.0D) {
                    bossBarMap.get(player.getUniqueId()).removePlayer(player);
                    bossBarMap.remove(player.getUniqueId());
                    cancel();
                } else {
                    countdown -= 1.0D;
                    bossBar.setProgress(countdown / 40.0D);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);

        bossBarMap.put(player.getUniqueId(), bossBar);
    }
}