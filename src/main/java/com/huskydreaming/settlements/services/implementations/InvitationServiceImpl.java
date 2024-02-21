package com.huskydreaming.settlements.services.implementations;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.InvitationService;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
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
    public void sendInvitation(Player player, Settlement settlement) {
        try {
            cache.get(player.getUniqueId(), HashSet::new).add(settlement.getName());

            TextComponent accept = new TextComponent(Locale.INVITATION_ACCEPT.parse());
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settlement accept " + settlement.getName()));

            TextComponent deny = new TextComponent(Locale.INVITATION_DENY.parse());
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settlement deny " + settlement.getName()));

            TextComponent spacer = new TextComponent(" ");

            player.sendMessage(Remote.prefix(Locale.INVITATION_DENIED, settlement.getName()));
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