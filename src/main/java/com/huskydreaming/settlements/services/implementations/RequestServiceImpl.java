package com.huskydreaming.settlements.services.implementations;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Singleton;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Request;
import com.huskydreaming.settlements.services.RequestService;
import com.huskydreaming.settlements.services.base.Service;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Singleton
public class RequestServiceImpl implements RequestService {

    private Cache<UUID, Request> cache;

    @Override
    public void removeRequest(Player player) {

    }

    @Override
    public void createRequest(Player player, Request.Type type) {

    }

    @Override
    public boolean hasRequest(Player player) {
        return false;
    }

    @Override
    public Request getRequest(Player player) {
        return null;
    }

    @Override
    public void serialize(SettlementPlugin plugin) {

    }

    @Override
    public void deserialize(SettlementPlugin plugin) {
        cache = CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build();
    }
}
