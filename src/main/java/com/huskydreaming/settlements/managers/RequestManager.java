package com.huskydreaming.settlements.managers;

import com.huskydreaming.settlements.persistence.Request;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RequestManager {

    private final Map<UUID, Request> requests = new ConcurrentHashMap<>();

    public Request getRequest(Player player) {
        return requests.get(player.getUniqueId());
    }

    public void create(Player player, Request.Type type) {
        Request request = Request.create(type);
        request.send(player);
        requests.put(player.getUniqueId(), request);
    }

    public boolean hasRequest(Player player) {
        return requests.containsKey(player.getUniqueId());
    }

    public void remove(Player player) {
        requests.remove(player.getUniqueId());
    }
}
