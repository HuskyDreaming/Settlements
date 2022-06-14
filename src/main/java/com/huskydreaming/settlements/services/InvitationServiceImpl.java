package com.huskydreaming.settlements.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Singleton;
import com.huskydreaming.settlements.persistence.Settlement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Singleton
public class InvitationServiceImpl implements InvitationService {

    private final Cache<UUID, Set<String>> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build();

    @Override
    public void sendInvitation(Player player, Settlement settlement) {
        try {
            cache.get(player.getUniqueId(), HashSet::new).add(settlement.getName());

            TextComponent accept = new TextComponent(ChatColor.GREEN + "[Accept]");
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settlement accept"));

            TextComponent deny = new TextComponent(ChatColor.RED + "[Deny]");
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settlement deny"));

            player.sendMessage(ChatColor.GRAY + "You have been invited to " + settlement.getName() + " do you wish to accept?");
            player.spigot().sendMessage(accept, deny);

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeInvitation(Player player, String string) {
        try {
            cache.get(player.getUniqueId(), HashSet::new).remove(string);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasNoInvitation(Player player, String string) {
        try {
            return cache.get(player.getUniqueId(), HashSet::new).stream().noneMatch(s -> s.equalsIgnoreCase(string));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
