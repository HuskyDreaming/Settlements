package com.huskydreaming.settlements.listeners;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.services.interfaces.BorderService;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
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

    public MemberListener(SettlementPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        claimService = plugin.provide(ClaimService.class);
        memberService = plugin.provide(MemberService.class);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Member member = memberService.getCitizen(player);
        if (member != null) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String lastOnline = df.format(today);

            member.setLastOnline(lastOnline);

            if (claimService.isClaim(player.getLocation().getChunk())) {
                String claim = claimService.getClaim(event.getPlayer().getLocation().getChunk());
                borderService.addPlayer(player, claim, claim.equalsIgnoreCase(member.getSettlement()) ? Color.AQUA : Color.RED);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Member member = memberService.getCitizen(player);
        if (member != null) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String lastOnline = df.format(today);

            member.setLastOnline(lastOnline);
            borderService.removePlayer(player);
        }
    }
}