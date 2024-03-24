package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import org.bukkit.entity.Player;

import java.util.Set;

public interface InvitationService extends Service {

    void sendInvitation(Player player, String name);

    void removeInvitation(Player player, String string);

    boolean hasNoInvitation(Player player, String string);

    Set<String> getInvitations(Player player);
}