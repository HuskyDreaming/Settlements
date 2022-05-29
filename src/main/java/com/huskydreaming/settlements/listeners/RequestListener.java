package com.huskydreaming.settlements.listeners;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.inventories.InventorySupplier;
import com.huskydreaming.settlements.persistence.Request;
import com.huskydreaming.settlements.persistence.Settlement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RequestListener implements Listener {

    private final Settlements settlements;

    public RequestListener(Settlements settlements) {
        this.settlements = settlements;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        settlements.getRequestManager().remove(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        settlements.getRequestManager().remove(event.getPlayer());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (settlements.getRequestManager().hasRequest(player)) {
            Settlement settlement = settlements.getSettlementManager().getSettlement(player);
            if (settlement != null) {
                Request request = settlements.getRequestManager().getRequest(player);
                Request.Response response = request.response(player, settlement, event.getMessage());
                Request.Response processedResponse = request.process(settlement, response);

                switch (processedResponse) {
                    case OK:
                    case CANCEL:
                        switch (request.getType()) {
                            case ROLE_CREATE:
                                Bukkit.getScheduler().runTask(settlements, () ->
                                        InventorySupplier.getRolesInventory(settlement).open(player)
                                );
                                break;
                        }

                        settlements.getRequestManager().remove(player);
                        break;
                }
            }
            event.setCancelled(true);
        }
    }
}
