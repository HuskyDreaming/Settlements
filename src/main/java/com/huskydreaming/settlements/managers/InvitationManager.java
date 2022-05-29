package com.huskydreaming.settlements.managers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.huskydreaming.settlements.persistence.Settlement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class InvitationManager {

    private final Cache<UUID, Set<String>> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build();

    public void removeInvitation(Player player, String string) {
        getInvitations(player).remove(string);
    }
    public boolean hasInvitation(Player player, String string) {
        return getInvitations(player).stream().anyMatch(s -> s.equalsIgnoreCase(string));
    }

    public Set<String> getInvitations(Player player) {
        try {
            return cache.get(player.getUniqueId(), HashSet::new);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendInvitation(Player player, Settlement settlement) {
        getInvitations(player).add(settlement.getName());

        TextComponent accept = new TextComponent(ChatColor.GREEN + "[Accept]");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settlement accept"));

        TextComponent deny = new TextComponent(ChatColor.RED + "[Deny]");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settlement deny"));

        player.sendMessage(ChatColor.GRAY + "You have been invited to " + settlement.getName() + " do you wish to accept?");
        player.spigot().sendMessage(accept, deny);
    }
}
