package com.huskydreaming.settlements.dependencies;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.database.entities.*;
import com.huskydreaming.settlements.database.persistence.Config;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.Placeholder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SettlementPlaceholderExpansion extends PlaceholderExpansion {

    private final HuskyPlugin plugin;
    private final ClaimService claimService;
    private final ConfigService configService;
    private final ContainerService containerService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public SettlementPlaceholderExpansion(HuskyPlugin plugin) {
        this.plugin = plugin;

        configService = plugin.provide(ConfigService.class);
        claimService = plugin.provide(ClaimService.class);
        containerService = plugin.provide(ContainerService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getDescription().getName().toLowerCase();
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (Placeholder.NAME.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getMember(player);
            Settlement settlement = settlementService.getSettlement(member);
            return settlement.getName();
        }

        if (Placeholder.TAG.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getMember(player);
            Settlement settlement = settlementService.getSettlement(member);
            Config config = configService.getConfig();
            String tag = settlement.getTag();
            return tag == null ? config.getEmptyPlaceholder() : tag;
        }

        if (Placeholder.OWNER.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getMember(player);
            Settlement settlement = settlementService.getSettlement(member);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(settlement.getOwnerUUID());
            return offlinePlayer.getName();
        }

        if (Placeholder.CLAIMS_COUNT.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getMember(player);
            Set<Claim> chunks = claimService.getClaims(member.getSettlementId());
            return String.valueOf(chunks.size());
        }

        if (Placeholder.ROLES_COUNT.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getMember(player);
            Settlement settlement = settlementService.getSettlement(member);
            Set<Role> roles = roleService.getRoles(settlement);
            return String.valueOf(roles.size());
        }

        if (Placeholder.MEMBERS_COUNT.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getMember(player);
            Set<Member> members = memberService.getMembers(member.getSettlementId());
            return String.valueOf(members.size());
        }

        if (Placeholder.CLAIMS_MAX.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getMember(player);
            Settlement settlement = settlementService.getSettlement(member);
            Container container = containerService.getContainer(settlement);
            return String.valueOf(container.getMaxClaims());
        }

        if (Placeholder.ROLES_MAX.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getMember(player);
            Settlement settlement = settlementService.getSettlement(member);
            Container container = containerService.getContainer(settlement);
            return String.valueOf(container.getMaxRoles());
        }

        if (Placeholder.MEMBERS_MAX.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getMember(player);
            Settlement settlement = settlementService.getSettlement(member);
            Container container = containerService.getContainer(settlement);
            return String.valueOf(container.getMaxMembers());
        }

        Config config = configService.getConfig();
        String defaultString = config.getEmptyPlaceholder();

        // This needs to be improved with caching?
        if (Placeholder.MEMBERS_TOP.containsPlaceholder(params)) {
            String[] split = params.split("_");
            if (split.length == 3 && Util.isNumeric(split[2])) {
                int number = Integer.parseInt(split[2]);
                if (memberService.getCount() < number) return defaultString;
                LinkedHashMap<Long, Long> map = memberService.getTop(number);

                if (map.isEmpty()) return defaultString;

                long last = -1;
                for (long key : map.keySet()) {
                    last = key;
                }

                Settlement settlement = settlementService.getSettlement(last);
                if (settlement != null) {
                    return settlement.getName();
                } else {
                    return "none";
                }
            }
        }

        // This needs to be improved with caching
        if (Placeholder.CLAIMS_TOP.containsPlaceholder(params)) {
            String[] split = params.split("_");
            if (split.length == 3 && Util.isNumeric(split[2])) {
                int number = Integer.parseInt(split[2]);
                if (claimService.getCount() < number) return defaultString;
                LinkedHashMap<Long, Long> map = claimService.getTop(number);

                if (map.isEmpty()) return defaultString;

                long last = -1;
                for (long key : map.keySet()) {
                    last = key;
                }

                Settlement settlement = settlementService.getSettlement(last);
                if (settlement != null) {
                    return settlement.getName();
                } else {
                    return "none";
                }
            }
        }
        return defaultString;
    }
}