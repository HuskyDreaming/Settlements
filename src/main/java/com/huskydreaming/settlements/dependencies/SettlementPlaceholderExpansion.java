package com.huskydreaming.settlements.dependencies;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.data.ChunkData;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.Placeholder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SettlementPlaceholderExpansion extends PlaceholderExpansion {

    private final HuskyPlugin plugin;
    private final ClaimService claimService;

    private final ConfigService configService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public SettlementPlaceholderExpansion(HuskyPlugin plugin) {
        this.plugin = plugin;

        configService = plugin.provide(ConfigService.class);
        claimService = plugin.provide(ClaimService.class);
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
            return memberService.getCitizen(player).getSettlement();
        }

        if (Placeholder.TAG.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            Config config = configService.getConfig();
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            String tag = settlement.getTag();
            return tag == null ? config.getEmptyPlaceholder() : tag;
        }

        if (Placeholder.OWNER.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            return settlement.getOwnerName();
        }

        if (Placeholder.CLAIMS_COUNT.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            Set<ChunkData> chunks = claimService.getClaims(member.getSettlement());
            return String.valueOf(chunks.size());
        }

        if (Placeholder.ROLES_COUNT.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            List<Role> roles = roleService.getRoles(member.getSettlement());
            return String.valueOf(roles.size());
        }

        if (Placeholder.MEMBERS_COUNT.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            List<Member> members = memberService.getMembers(member.getSettlement());
            return String.valueOf(members.size());
        }

        if (Placeholder.CLAIMS_MAX.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            return String.valueOf(settlement.getMaxLand());
        }

        if (Placeholder.ROLES_MAX.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            return String.valueOf(settlement.getMaxRoles());
        }

        if (Placeholder.MEMBERS_MAX.isPlaceholder(params) && memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            return String.valueOf(settlement.getMaxCitizens());
        }

        Config config = configService.getConfig();
        String defaultString = config.getEmptyPlaceholder();

        // This needs to be improved with caching
        if (Placeholder.MEMBERS_TOP.containsPlaceholder(params)) {
            String[] split = params.split("_");
            if (split.length == 3 && Util.isNumeric(split[2])) {
                int number = Integer.parseInt(split[2]);
                if (memberService.getCount() < number) return defaultString;
                LinkedHashMap<String, Long> map = memberService.getTop(number);

                if (map.isEmpty()) return defaultString;

                String last = null;
                for (String key : map.keySet()) {
                    last = key;
                }

                return last;
            }
        }

        // This needs to be improved with caching
        if (Placeholder.CLAIMS_TOP.containsPlaceholder(params)) {
            String[] split = params.split("_");
            if (split.length == 3 && Util.isNumeric(split[2])) {
                int number = Integer.parseInt(split[2]);
                if (claimService.getCount() < number) return defaultString;
                LinkedHashMap<String, Long> map = claimService.getTop(number);

                if (map.isEmpty()) return defaultString;

                String last = null;
                for (String key : map.keySet()) {
                    last = key;
                }

                return last;
            }
        }
        return defaultString;
    }
}