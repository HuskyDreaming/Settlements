package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.database.entities.Settlement;
import org.bukkit.entity.Player;

import java.util.Set;

public interface InvitationService extends Service {

    void sendInvitation(Player player, Settlement settlement);

    void removeInvitation(Player player, Settlement settlement);

    boolean hasNoInvitation(Player player, Settlement settlement);

    Set<Long> getInvitations(Player player);
}