package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import org.bukkit.entity.Player;

public interface InvitationService extends ServiceInterface {

    void sendInvitation(Player player, Settlement settlement);

    void removeInvitation(Player player, String string);

    boolean hasNoInvitation(Player player, String string);
}
