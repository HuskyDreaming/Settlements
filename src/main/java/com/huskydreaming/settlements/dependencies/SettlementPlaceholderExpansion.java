package com.huskydreaming.settlements.dependencies;

import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class SettlementPlaceholderExpansion extends PlaceholderExpansion {

    private final JavaPlugin plugin;

    private final ClaimService claimService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public SettlementPlaceholderExpansion(JavaPlugin plugin) {
        this.plugin = plugin;
        claimService = ServiceProvider.Provide(ClaimService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        roleService = ServiceProvider.Provide(RoleService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
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
        if(params.equalsIgnoreCase("name")) {
            if(memberService.hasSettlement(player)) return memberService.getCitizen(player).getSettlement();
        }

        if(params.equalsIgnoreCase("owner")) {
            if(memberService.hasSettlement(player)) {
                Member member = memberService.getCitizen(player);
                Settlement settlement = settlementService.getSettlement(member.getSettlement());
                return settlement.getOwnerName();
            }
        }

        if(params.equalsIgnoreCase("claims_count")) {
            if(memberService.hasSettlement(player)) {
                Member member = memberService.getCitizen(player);
                Settlement settlement = settlementService.getSettlement(member.getSettlement());
                Collection<Chunk> chunks = claimService.getChunks(settlement);
                return String.valueOf(chunks.size());
            }
        }

        if(params.equalsIgnoreCase("roles_count")) {
            if(memberService.hasSettlement(player)) {
                Member member = memberService.getCitizen(player);
                Settlement settlement = settlementService.getSettlement(member.getSettlement());
                List<Role> roles = roleService.getRoles(settlement);
                return String.valueOf(roles.size());
            }
        }

        if(params.equalsIgnoreCase("members_count")) {
            if(memberService.hasSettlement(player)) {
                Member member = memberService.getCitizen(player);
                Settlement settlement = settlementService.getSettlement(member.getSettlement());
                List<Member> members = memberService.getMembers(settlement);
                return String.valueOf(members.size());
            }
        }

        if(params.equalsIgnoreCase("claims_max")) {
            if(memberService.hasSettlement(player)) {
                Member member = memberService.getCitizen(player);
                Settlement settlement = settlementService.getSettlement(member.getSettlement());
                return String.valueOf(settlement.getMaxLand());
            }
        }

        if(params.equalsIgnoreCase("roles_max")) {
            if(memberService.hasSettlement(player)) {
                Member member = memberService.getCitizen(player);
                Settlement settlement = settlementService.getSettlement(member.getSettlement());
                return String.valueOf(settlement.getMaxRoles());
            }
        }

        if(params.equalsIgnoreCase("members_max")) {
            if(memberService.hasSettlement(player)) {
                Member member = memberService.getCitizen(player);
                Settlement settlement = settlementService.getSettlement(member.getSettlement());
                return String.valueOf(settlement.getMaxCitizens());
            }
        }
        return null;
    }
}
