package com.huskydreaming.settlements.listeners;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Request;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RequestService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RequestListener implements Listener {

    private SettlementPlugin settlementPlugin;

    private final MemberService memberService;

    private final RequestService requestService;

    private final SettlementService settlementService;

    public RequestListener() {
        memberService = ServiceProvider.Provide(MemberService.class);
        requestService = ServiceProvider.Provide(RequestService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }


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
            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            if (settlement != null) {
                Request request = requestService.getRequest(player);
                Request.Response response = request.response(player, settlement, event.getMessage());
                Request.Response processedResponse = request.process(settlement, response);

                switch (processedResponse) {
                    case OK, CANCEL -> {
                        switch (request.getType()) {
                            case ROLE_CREATE:
                                // OPEN INVENTORY
                                //Bukkit.getScheduler().runTask(plugin, null);
                                break;
                        }
                        requestService.removeRequest(player);
                    }
                }
            }
            event.setCancelled(true);
        }
    }
}
