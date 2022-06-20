package com.huskydreaming.settlements.services;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.Service;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import com.huskydreaming.settlements.services.base.ServiceType;
import org.bukkit.entity.Player;

@Service(type = ServiceType.INVITATION)
public interface InvitationService extends ServiceInterface {

    void sendInvitation(Player player, Settlement settlement);

    void removeInvitation(Player player, String string);

    boolean hasNoInvitation(Player player, String string);
}
