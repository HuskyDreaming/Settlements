package com.huskydreaming.settlements.services.implementations;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.services.interfaces.InvitationService;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class InvitationServiceImpl implements InvitationService {

    private Cache<UUID, Set<String>> cache;

    @Override
    public void sendInvitation(Player player, String name) {
        try {
            cache.get(player.getUniqueId(), HashSet::new).add(name);

            TextComponent accept = new TextComponent(Locale.INVITATION_ACCEPT.parse());
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settlement accept " + name));

            TextComponent deny = new TextComponent(Locale.INVITATION_DENY.parse());
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settlement deny " + name));

            TextComponent spacer = new TextComponent(" ");

            player.sendMessage(Locale.INVITATION_DENIED.prefix(name));
            player.spigot().sendMessage(accept, spacer, deny);

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

    @Override
    public void deserialize(SettlementPlugin plugin) {
        cache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .build();
    }
}