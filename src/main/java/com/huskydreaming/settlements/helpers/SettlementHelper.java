package com.huskydreaming.settlements.helpers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.database.base.DatabaseConnector;
import com.huskydreaming.huskycore.interfaces.database.base.DatabaseService;
import com.huskydreaming.huskycore.utilities.AsyncAction;
import com.huskydreaming.settlements.database.dao.*;
import com.huskydreaming.settlements.database.entities.*;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.*;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

public class SettlementHelper {

    public static AsyncAction<Long> createSettlement(Player player, String name, HuskyPlugin plugin) {
        DatabaseConnector databaseConnector = plugin.provide(DatabaseService.class).getDatabaseConnector();
        ClaimService claimService = plugin.provide(ClaimService.class);
        ContainerService containerService = plugin.provide(ContainerService.class);
        MemberService memberService = plugin.provide(MemberService.class);
        PermissionService permissionService = plugin.provide(PermissionService.class);
        RoleService roleService = plugin.provide(RoleService.class);
        SettlementService settlementService = plugin.provide(SettlementService.class);
        HomeService homeService = plugin.provide(HomeService.class);

        Settlement settlement = settlementService.createSettlement(player, name);
        Member member = memberService.createMember(player);
        Home home = homeService.createHome(player, "spawn");
        Role role = roleService.createDefaultRole();
        Container container = containerService.createDefaultContainer();
        Claim claim = claimService.createClaim(player.getLocation().getChunk());
        Permission permission = permissionService.createPermission(PermissionType.SPAWN_TELEPORT);

        SettlementDao settlementDao = settlementService.getDao();
        ContainerDao containerDao = containerService.getDao();
        MemberDao memberDao = memberService.getDao();
        RoleDao roleDao = roleService.getDao();
        HomeDao homeDao = homeService.getDao();
        ClaimDao claimDao = claimService.getDao();
        PermissionDao permissionDao = permissionService.getDao();

        return AsyncAction.supplyAsync(plugin, () -> {
            try(Connection connection = databaseConnector.getConnection()) {
                AtomicLong atomicLong = new AtomicLong();
                settlementDao.insertStatement(settlement, connection, settlementId -> {
                    settlement.setId(settlementId);
                    container.setSettlementId(settlementId);
                    member.setSettlementId(settlementId);
                    claim.setSettlementId(settlementId);
                    role.setSettlementId(settlementId);
                    home.setSettlementId(settlementId);
                    atomicLong.set(settlementId);
                });

                roleDao.insertStatement(role, connection, roleId -> {
                    role.setId(roleId);
                    member.setRoleId(roleId);
                    permission.setRoleId(roleId);
                    settlement.setRoleId(roleId);
                });


                containerDao.insertStatement(container, connection, container::setId);
                homeDao.insertStatement(home, connection, home::setId);
                memberDao.insertStatement(member, connection, member::setId);
                claimDao.insertStatement(claim, connection, claim::setId);
                permissionDao.insertStatement(permission, connection, permission::setId);

                containerService.addContainer(container);
                roleService.addRole(role);
                memberService.addMember(member);
                permissionService.addPermission(permission);
                settlementService.addSettlement(settlement);
                claimService.addClaim(claim);
                homeService.addHome(home);
                return atomicLong.get();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
