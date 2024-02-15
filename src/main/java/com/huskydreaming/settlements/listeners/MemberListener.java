package com.huskydreaming.settlements.listeners;

import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MemberListener implements Listener {

    private final MemberService memberService;

    public MemberListener() {
        memberService = ServiceProvider.Provide(MemberService.class);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Member member = memberService.getCitizen(event.getPlayer());
        if(member != null) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String lastOnline = df.format(today);

            member.setLastOnline(lastOnline);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Member member = memberService.getCitizen(event.getPlayer());
        if(member != null) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String lastOnline = df.format(today);

            member.setLastOnline(lastOnline);
        }
    }
}
