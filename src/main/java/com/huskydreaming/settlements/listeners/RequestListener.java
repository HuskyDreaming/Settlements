package com.huskydreaming.settlements.listeners;

import com.google.inject.Inject;
import com.huskydreaming.settlements.persistence.Citizen;
import com.huskydreaming.settlements.persistence.Request;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.CitizenService;
import com.huskydreaming.settlements.services.RequestService;
import com.huskydreaming.settlements.services.SettlementService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class RequestListener implements Listener {

    @Inject
    private Plugin plugin;

    @Inject
    private CitizenService citizenService;

    @Inject
    private RequestService requestService;

    @Inject
    private SettlementService settlementService;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        requestService.removeRequest(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        requestService.removeRequest(event.getPlayer());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (requestService.hasRequest(player)) {
            Citizen citizen = citizenService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(citizen.getSettlement());
            if (settlement != null) {
                Request request = requestService.getRequest(player);
                Request.Response response = request.response(player, settlement, event.getMessage());
                Request.Response processedResponse = request.process(settlement, response);

                switch (processedResponse) {
                    case OK:
                    case CANCEL:
                        switch (request.getType()) {
                            case ROLE_CREATE:
                                // OPEN INVENTORY
                                //Bukkit.getScheduler().runTask(plugin, null);
                                break;
                        }

                        requestService.removeRequest(player);
                        break;
                }
            }
            event.setCancelled(true);
        }
    }
}
