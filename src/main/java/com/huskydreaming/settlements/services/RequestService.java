package com.huskydreaming.settlements.services;

import com.huskydreaming.settlements.persistence.Request;
import org.bukkit.entity.Player;

public interface RequestService {

    void removeRequest(Player player);

    void createRequest(Player player, Request.Type type);
    boolean hasRequest(Player player);

    Request getRequest(Player player);
}
