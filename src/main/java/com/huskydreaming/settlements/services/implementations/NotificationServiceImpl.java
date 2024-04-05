package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.NotificationService;
import com.huskydreaming.settlements.storage.types.Locale;
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
    public void sendTrust(Player player, String name, String description) {
        String capitalizedChunk = Util.capitalize(name);

        Config config = configService.getConfig();
        switch (config.getNotificationType()) {
            case TITLE -> {
                String header = Locale.NOTIFICATION_TITLE_HEADER.parameterize(ChatColor.YELLOW, capitalizedChunk);
                String footer = Locale.NOTIFICATION_TITLE_FOOTER.parameterize(description);
                sendTitle(player, header, footer);
            }
            case BOSS_BAR -> {
                String message = Locale.NOTIFICATION_BOSS_BAR.parameterize(ChatColor.YELLOW, capitalizedChunk);
                sendBossBar(player, BarColor.YELLOW, message);
            }
            case ACTION_BAR -> {
                String message = Locale.NOTIFICATION_ACTION_BAR.parameterize(ChatColor.YELLOW, capitalizedChunk);
                sendActionBar(player, message);
            }
        }
    }

    @Override
    public void sendWilderness(Player player) {
        Config config = configService.getConfig();
        switch (config.getNotificationType()) {
            case TITLE -> {
                String header = Locale.NOTIFICATION_WILDERNESS_TITLE_HEADER.parse();
                String footer = Locale.NOTIFICATION_WILDERNESS_TITLE_FOOTER.parse();
                sendTitle(player, header, footer);
            }
            case BOSS_BAR -> {
                String title = Locale.NOTIFICATION_WILDERNESS_BOSS_BAR.parse();
                sendBossBar(player, BarColor.GREEN, title);
            }
            case ACTION_BAR -> {
                String title = Locale.NOTIFICATION_WILDERNESS_ACTION_BAR.parse();
                sendActionBar(player, title);
            }
        }
    }

    @Override
    public void sendSettlement(Player player, String chunk, String description, boolean isClaim) {
        BarColor barColor = isClaim ? BarColor.BLUE : BarColor.RED;
        ChatColor chatColor = isClaim ? ChatColor.AQUA : ChatColor.RED;
        String capitalizedChunk = Util.capitalize(chunk);

        Config config = configService.getConfig();
        switch (config.getNotificationType()) {
            case TITLE -> {
                String header = Locale.NOTIFICATION_TITLE_HEADER.parameterize(chatColor, capitalizedChunk);
                String footer = Locale.NOTIFICATION_TITLE_FOOTER.parameterize(description);
                sendTitle(player, header, footer);
            }
            case BOSS_BAR -> {
                String message = Locale.NOTIFICATION_BOSS_BAR.parameterize(chatColor, capitalizedChunk);
                sendBossBar(player, barColor, message);
            }
            case ACTION_BAR -> {
                String message = Locale.NOTIFICATION_ACTION_BAR.parameterize(chatColor, capitalizedChunk);
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