package com.huskydreaming.settlements.services.implementations;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.services.interfaces.InvitationService;
import com.huskydreaming.settlements.enumeration.locale.Message;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class InvitationServiceImpl implements InvitationService {

    private Cache<UUID, Set<Long>> cache;

    @Override
    public void sendInvitation(Player player, Settlement settlement) {
        try {
            cache.get(player.getUniqueId(), HashSet::new).add(settlement.getId());

            TextComponent accept = new TextComponent(Message.INVITATION_ACCEPT.parse());
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settlement accept " + settlement.getName()));

            TextComponent deny = new TextComponent(Message.INVITATION_DENY.parse());
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settlement deny " + settlement.getName()));

            TextComponent spacer = new TextComponent(" ");

            player.sendMessage(Message.INVITATION_DENIED.prefix(settlement.getName()));
            player.spigot().sendMessage(accept, spacer, deny);

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeInvitation(Player player, Settlement settlement) {
        try {
            cache.get(player.getUniqueId(), HashSet::new).remove(settlement.getId());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasNoInvitation(Player player, Settlement settlement) {
        try {
            return cache.get(player.getUniqueId(), HashSet::new).stream().noneMatch(s -> s == settlement.getId());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Long> getInvitations(Player player) {
        return cache.getIfPresent(player.getUniqueId());
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        cache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .build();
    }
}