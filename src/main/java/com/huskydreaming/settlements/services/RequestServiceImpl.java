package com.huskydreaming.settlements.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Singleton;
import com.huskydreaming.settlements.persistence.Request;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Singleton
public class RequestServiceImpl implements RequestService{

    private final Cache<UUID, Request> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();

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
}
