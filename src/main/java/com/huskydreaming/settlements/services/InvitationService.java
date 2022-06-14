package com.huskydreaming.settlements.services;

import com.huskydreaming.settlements.persistence.Settlement;
import org.bukkit.entity.Player;

public interface InvitationService {

    void sendInvitation(Player player, Settlement settlement);

    void removeInvitation(Player player, String string);

    boolean hasNoInvitation(Player player, String string);
}
