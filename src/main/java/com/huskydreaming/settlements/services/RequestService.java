package com.huskydreaming.settlements.services;

import com.huskydreaming.settlements.persistence.Request;
import com.huskydreaming.settlements.services.base.Service;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import com.huskydreaming.settlements.services.base.ServiceType;
import org.bukkit.entity.Player;

@Service(type = ServiceType.REQUEST)
public interface RequestService extends ServiceInterface {

    void removeRequest(Player player);

    void createRequest(Player player, Request.Type type);
    boolean hasRequest(Player player);

    Request getRequest(Player player);
}
