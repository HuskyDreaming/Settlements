package com.huskydreaming.settlements.listeners;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.entities.Claim;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.services.interfaces.*;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MemberListener implements Listener {

    private final BorderService borderService;
    private final ClaimService claimService;
    private final MemberService memberService;
    private final SettlementService settlementService;
    private final TrustService trustService;

    public MemberListener(SettlementPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        claimService = plugin.provide(ClaimService.class);
        memberService = plugin.provide(MemberService.class);
        settlementService = plugin.provide(SettlementService.class);
        trustService = plugin.provide(TrustService.class);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Member member = memberService.getMember(player);
        if (member != null) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String lastOnline = df.format(today);

            member.setLastOnline(lastOnline);

            if (claimService.isClaim(player.getLocation().getChunk())) {
                Claim claim = claimService.getClaim(event.getPlayer().getLocation().getChunk());
                Settlement settlement = settlementService.getSettlement(member);

                if(claim.getSettlementId() == (member.getSettlementId())){
                    borderService.addPlayer(player, settlement, Color.AQUA);
                } else if(trustService.getOfflinePlayers(settlement).contains(player)) {
                    borderService.addPlayer(player, settlement, Color.YELLOW);
                } else {
                    borderService.addPlayer(player, settlement, Color.RED);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Member member = memberService.getMember(player);
        if (member != null) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String lastOnline = df.format(today);

            member.setLastOnline(lastOnline);
            borderService.removePlayer(player);
        }
    }
}