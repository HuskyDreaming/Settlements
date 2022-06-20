package com.huskydreaming.settlements.listeners;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Request;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.InventoryService;
import com.huskydreaming.settlements.services.RequestService;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.services.base.ServiceRegistry;
import com.huskydreaming.settlements.services.base.ServiceType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RequestListener implements Listener {

    private final SettlementPlugin settlementPlugin;

    public RequestListener(SettlementPlugin settlementPlugin) {
        this.settlementPlugin = settlementPlugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        RequestService requestService = (RequestService) ServiceRegistry.getService(ServiceType.REQUEST);
        requestService.removeRequest(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        RequestService requestService = (RequestService) ServiceRegistry.getService(ServiceType.REQUEST);
        requestService.removeRequest(event.getPlayer());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        RequestService requestService = (RequestService) ServiceRegistry.getService(ServiceType.REQUEST);
        if (requestService.hasRequest(player)) {
            SettlementService settlementService = (SettlementService) ServiceRegistry.getService(ServiceType.SETTLEMENT);
            Settlement settlement = settlementService.getSettlement(player);
            if (settlement != null) {
                Request request = requestService.getRequest(player);
                Request.Response response = request.response(player, settlement, event.getMessage());
                Request.Response processedResponse = request.process(settlement, response);

                InventoryService inventoryService = (InventoryService) ServiceRegistry.getService(ServiceType.INVENTORY);
                switch (processedResponse) {
                    case OK:
                    case CANCEL:
                        switch (request.getType()) {
                            case ROLE_CREATE:
                                Bukkit.getScheduler().runTask(settlementPlugin, () ->
                                        inventoryService.getRolesInventory(settlement).open(player)
                                );
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
