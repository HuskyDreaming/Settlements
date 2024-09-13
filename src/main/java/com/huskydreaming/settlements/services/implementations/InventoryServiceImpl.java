package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.inventories.InventoryModuleProvider;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.database.entities.*;
import com.huskydreaming.settlements.database.persistence.Config;
import com.huskydreaming.settlements.enumeration.filters.MemberFilter;
import com.huskydreaming.settlements.enumeration.types.SettlementDefaultType;
import com.huskydreaming.settlements.inventories.base.InventoryAction;
import com.huskydreaming.settlements.inventories.base.InventoryActionType;
import com.huskydreaming.settlements.inventories.modules.admin.*;
import com.huskydreaming.settlements.inventories.modules.general.*;
import com.huskydreaming.settlements.inventories.providers.SettlementInventory;
import com.huskydreaming.settlements.inventories.providers.*;
import com.huskydreaming.settlements.enumeration.FlagType;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.*;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InventoryServiceImpl implements InventoryService {

    private InventoryManager manager;
    private final ClaimService claimService;
    private final ConfigService configService;
    private final HomeService homeService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;
    private final TrustService trustService;
    private final Map<UUID, InventoryAction> actions = new ConcurrentHashMap<>();

    public InventoryServiceImpl(HuskyPlugin plugin) {
        claimService = plugin.provide(ClaimService.class);
        configService = plugin.provide(ConfigService.class);
        homeService = plugin.provide(HomeService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        trustService = plugin.provide(TrustService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        manager = new InventoryManager(plugin);
        manager.init();
    }

    @Override
    public void addAction(Player player, InventoryAction action) {
        this.actions.put(player.getUniqueId(), action);
    }

    @Override
    public void acceptAction(Player player) {
        actions.get(player.getUniqueId()).onAccept(player);
        actions.remove(player.getUniqueId());
    }

    @Override
    public void denyAction(Player player) {
        actions.get(player.getUniqueId()).onDeny(player);
        actions.remove(player.getUniqueId());
    }

    @Override
    public InventoryActionType getActionType(Player player) {
        return actions.get(player.getUniqueId()).getType();
    }

    @Override
    public SmartInventory getAdminInventory(Player player, HuskyPlugin plugin) {
        InventoryModuleProvider adminInventory = new InventoryModuleProvider();

        adminInventory.deserialize(player,
                new AdminDefaultsModule(plugin),
                new AdminDisabledWorldsModule(plugin),
                new AdminNotificationModule(plugin),
                new AdminTeleportationModule(plugin),
                new AdminTrustingModule(plugin)
        );

        return SmartInventory.builder()
                .manager(manager)
                .id("adminInventory")
                .size(Math.min(adminInventory.getRows() + 2, 5), 9)
                .provider(adminInventory)
                .title("Admin Panel")
                .build();
    }

    @Override
    public SmartInventory getWorldsInventory(HuskyPlugin plugin) {
        List<String> worlds = Bukkit.getWorlds().stream().map(World::getName).toList();
        String[] disabledWorlds = worlds.toArray(new String[0]);
        int rows = (int) Math.ceil((double) disabledWorlds.length / 9);

        WorldsInventory worldsInventory = new WorldsInventory(plugin, rows);
        worldsInventory.setArray(disabledWorlds);
        return SmartInventory.builder()
                .manager(manager)
                .id("worldsInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(worldsInventory)
                .title("Worlds")
                .build();
    }

    @Override
    public SmartInventory getRoleInventory(HuskyPlugin plugin, Role role) {
        PermissionType[] permissionTypes = PermissionType.values();
        PermissionType[] permissions = Arrays.copyOfRange(permissionTypes, 1, permissionTypes.length);
        int rows = (int) Math.ceil((double) permissions.length / 9);
        RoleInventory roleInventory = new RoleInventory(plugin, rows, role, permissions);
        return SmartInventory.builder()
                .manager(manager)
                .id("roleInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(roleInventory)
                .title("Editing: " + Util.capitalize(role.getName()))
                .build();
    }

    @Override
    public SmartInventory getMainInventory(HuskyPlugin plugin, Player player) {
        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);
        Config config = configService.getConfig();
        SettlementInventory settlementInventory = new SettlementInventory(plugin);

        List<InventoryModule> modules = new ArrayList<>();
        modules.add(new MembersModule(plugin));
        modules.add(new ClaimModule(plugin));
        modules.add(new RoleModule(plugin));
        modules.add(new InformationModule(plugin));
        modules.add(new FlagModule(plugin));

        if(config.isHomes() && homeService.hasHomes(settlement)) {
            if(!homeService.getHomes(settlement).isEmpty()) {
                modules.add(new HomeModule(plugin));
            }
        }

        if(config.isTeleportation()) {
            modules.add(new SpawnModule(plugin));
        }

        settlementInventory.deserialize(player, modules);

        return SmartInventory.builder()
                .id("settlementInventory")
                .manager(manager)
                .size(Math.min(settlementInventory.getRows() + 2, 5), 9)
                .provider(settlementInventory)
                .title("Editing: " + Util.capitalize(settlement.getName()))
                .build();
    }

    @Override
    public SmartInventory getSettlementsInventory(HuskyPlugin plugin) {
        Long[] settlements = settlementService.getSettlements().keySet().toArray(new Long[0]);
        int rows = (int) Math.ceil((double) settlements.length / 9);
        SettlementsInventory settlementsInventory = new SettlementsInventory(plugin, rows, settlements);
        return SmartInventory.builder()
                .manager(manager)
                .id("settlementsInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(settlementsInventory)
                .title("Settlements")
                .build();
    }

    @Override
    public SmartInventory getConfirmationInventory(HuskyPlugin plugin, Player player) {
        ConfirmationInventory confirmationInventory = new ConfirmationInventory(plugin);
        return SmartInventory.builder()
                .manager(manager)
                .id("confirmationInventory")
                .size(3, 9)
                .provider(confirmationInventory)
                .title(getActionType(player).getTitle())
                .build();
    }

    @Override
    public SmartInventory getRolesInventory(HuskyPlugin plugin, Player player) {
        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);
        Set<Role> roles = roleService.getRoles(settlement);

        Role[] array = roles.toArray(new Role[0]);
        int rows = (int) Math.ceil((double) array.length / 9);

        RolesInventory rolesInventory = new RolesInventory(plugin, rows, array);
        return SmartInventory.builder()
                .manager(manager)
                .id("rolesInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(rolesInventory)
                .title("Roles")
                .build();
    }

    @Override
    public SmartInventory getHomesInventory(HuskyPlugin plugin, Player player) {
        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);

        Set<Home> homes = new HashSet<>();
        if(homeService.hasHomes(settlement)) {
            homes = homeService.getHomes(settlement);
        } else {
            homeService.setHome(settlement, player, "spawn");
        }

        Home[] finalHomes = homes.toArray(new Home[0]);

        int rows = (int) Math.ceil((double) finalHomes.length / 9);
        HomesInventory homesInventory = new HomesInventory(plugin, rows);
        homesInventory.setArray(finalHomes);

        return SmartInventory.builder()
                .manager(manager)
                .id("homesInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(homesInventory)
                .title("Homes")
                .build();
    }

    @Override
    public SmartInventory getFlagsInventory(HuskyPlugin plugin, Player player) {
        FlagType[] flagTypes = FlagType.values();
        int rows = (int) Math.ceil((double) flagTypes.length / 9);
        FlagsInventory flagsInventory = new FlagsInventory(plugin, rows, flagTypes);
        return SmartInventory.builder()
                .manager(manager)
                .id("flagsInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(flagsInventory)
                .title("Flags")
                .build();
    }

    @Override
    public SmartInventory getClaimsInventory(HuskyPlugin plugin, Player player) {
        Member member = memberService.getMember(player);
        Claim[] claims = claimService.getClaims(member.getSettlementId()).toArray(new Claim[0]);
        int rows = (int) Math.ceil((double) claims.length / 9);
        ClaimsInventory claimsInventory = new ClaimsInventory(plugin, rows, claims);

        return SmartInventory.builder()
                .manager(manager)
                .id("claimsInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(claimsInventory)
                .title("Claims")
                .build();
    }

    @Override
    public SmartInventory getSettlementDefaultsInventory(HuskyPlugin plugin) {
        SettlementDefaultType[] types = SettlementDefaultType.values();
        int rows = (int) Math.ceil((double) types.length / 9);
        DefaultsInventory defaultsInventory = new DefaultsInventory(plugin, rows);
        defaultsInventory.setArray(types);

        return SmartInventory.builder()
                .manager(manager)
                .id("defaultsInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(defaultsInventory)
                .title("Defaults")
                .build();
    }

    @Override
    public SmartInventory getMembersInventory(HuskyPlugin plugin, Player player, MemberFilter memberFilter) {
        Member member = memberService.getMember(player);
        Set<OfflinePlayer> trustedOfflinePlayers = new HashSet<>();
        Config config = configService.getConfig();

        if (config.isTrusting()) trustedOfflinePlayers = trustService.getOfflinePlayers(member.getSettlementId());
        Set<OfflinePlayer> memberOfflinePlayers = memberService.getOfflinePlayers(member.getSettlementId());

        Set<OfflinePlayer> offlinePlayers = switch (memberFilter) {
            case ALL -> Stream.concat(trustedOfflinePlayers.stream(), memberOfflinePlayers.stream()).collect(Collectors.toSet());
            case MEMBER -> memberOfflinePlayers;
            case TRUSTED -> trustedOfflinePlayers;
        };

        int rows = (int) Math.ceil((double) offlinePlayers.size() / 9);
        OfflinePlayer[] array = offlinePlayers.toArray(new OfflinePlayer[0]);
        MembersInventory membersInventory = new MembersInventory(plugin, rows, array);
        membersInventory.setMemberFilter(memberFilter);

        return SmartInventory.builder()
                .manager(manager)
                .id("membersInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(membersInventory)
                .title("Members")
                .build();
    }

    @Override
    public SmartInventory getMemberInventory(HuskyPlugin plugin, OfflinePlayer offlinePlayer) {
        MemberInventory memberInventory = new MemberInventory(plugin, offlinePlayer);
        return SmartInventory.builder()
                .manager(manager)
                .id("memberInventory")
                .size(3, 9)
                .provider(memberInventory)
                .title("Editing: " + Util.capitalize(offlinePlayer.getName()))
                .build();
    }
}