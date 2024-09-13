package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.database.persistence.Config;
import com.huskydreaming.settlements.enumeration.filters.MemberFilter;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.enumeration.locale.Message;
import com.huskydreaming.settlements.enumeration.locale.Menu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Set;
import java.util.List;

public class MemberInventory implements InventoryProvider {

    private final HuskyPlugin plugin;
    private final ConfigService configService;
    private final InventoryService inventoryService;
    private final PermissionService permissionService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;
    private final OfflinePlayer offlinePlayer;

    public MemberInventory(HuskyPlugin plugin, OfflinePlayer offlinePlayer) {
        this.plugin = plugin;

        configService = plugin.provide(ConfigService.class);
        inventoryService = plugin.provide(InventoryService.class);
        permissionService = plugin.provide(PermissionService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);

        this.offlinePlayer = offlinePlayer;
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        if(!memberService.hasSettlement(player)) return;

        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);

        contents.fillBorders(InventoryItem.border());
        contents.set(0, 0, InventoryItem.back(player, inventoryService.getMembersInventory(plugin, player, MemberFilter.ALL)));
        contents.set(1, 3, setOwner(settlement, contents));
        contents.set(1, 4, roleItem(contents));
        contents.set(1, 5, kickItem(contents));

        Config config = configService.getConfig();
        if(config.isTeleportation() && offlinePlayer.isOnline() && offlinePlayer.getPlayer() != player) {
            contents.set(1, 1, teleportItem(player));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ClickableItem setOwner(Settlement settlement, InventoryContents contents) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(settlement.getOwnerUUID());
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.MEMBER_SET_OWNER_TITLE.parse())
                .setLore(Menu.MEMBER_SET_OWNER_LORE.parameterizeList(offlinePlayer.getName()))
                .setMaterial(Material.EMERALD)
                .build(), e -> setOwnerClick(e, contents));
    }

    private void setOwnerClick(InventoryClickEvent event, InventoryContents contents) {
        if(event.getWhoClicked() instanceof Player player) {
            if (!memberService.hasSettlement(player)) return;
            if (!memberService.hasSettlement(offlinePlayer)) return;

            Member member = memberService.getMember(offlinePlayer);
            Settlement settlement = settlementService.getSettlement(member);
            if (settlement.isOwner(player)) {

                if (offlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                    player.sendMessage(Message.OWNER_CURRENT.prefix());
                } else {
                    Player target = offlinePlayer.getPlayer();
                    if (target != null) {
                        target.sendMessage(Message.OWNER.prefix());
                    }
                    player.sendMessage(Message.OWNER_TRANSFERRED.prefix(offlinePlayer.getName()));
                    settlement.setOwner(offlinePlayer);
                }
            } else {
                player.sendMessage(Message.OWNER_TRANSFER_FALSE.prefix());
            }
            contents.inventory().close(player);
        }
    }

    private ClickableItem teleportItem(Player player) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.MEMBER_TELEPORT_TITLE.parse())
                .setLore(Menu.MEMBER_TELEPORT_LORE.parseList())
                .setMaterial(Material.ENDER_PEARL)
                .build(), e -> {

            Player onlinePlayer = offlinePlayer.getPlayer();
            if(onlinePlayer != null) {
                player.teleport(onlinePlayer);
                player.sendMessage(Message.PLAYER_TELEPORT.prefix(offlinePlayer.getName()));
            }
        });
    }

    private ClickableItem roleItem(InventoryContents contents) {
        Member member = memberService.getMember(offlinePlayer);
        Settlement settlement = settlementService.getSettlement(member);
        Role role = roleService.getRole(member);
        long index = roleService.getIndex(settlement, role) + 1;
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.MEMBER_SET_ROLE_TITLE.parse())
                .setLore(Menu.MEMBER_SET_ROLE_LORE.parameterizeList(index, role.getName()))
                .setMaterial(Material.WRITABLE_BOOK)
                .build(), e -> roleItemClick(e, contents));
    }

    private void roleItemClick(InventoryClickEvent event, InventoryContents contents) {
        if(event.getWhoClicked() instanceof Player player) {
            if (!memberService.hasSettlement(player)) return;
            if (!memberService.hasSettlement(offlinePlayer)) return;

            Member member = memberService.getMember(offlinePlayer);
            Settlement settlement = settlementService.getSettlement(member);
            Role role = roleService.sync(member, settlement);
            List<Role> roleList = roleService.getRoleList(settlement);
            if (event.isRightClick()) {
                memberService.demote(role, member, roleList, () -> contents.inventory().open(player));
            } else if (event.isLeftClick()) {
                memberService.promote(role, member, roleList, ()-> contents.inventory().open(player));
            }
        }
    }

    private ClickableItem kickItem(InventoryContents contents) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.MEMBER_KICK_TITLE.parse())
                .setLore(Menu.MEMBER_KICK_LORE.parseList())
                .setMaterial(Material.ANVIL)
                .build(), e -> kickItemClick(e, contents));
    }

    private void kickItemClick(InventoryClickEvent event, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if (!memberService.hasSettlement(player)) return;
            if (!memberService.hasSettlement(offlinePlayer)) return;

            Member member = memberService.getMember(offlinePlayer);
            Role role = roleService.getRole(member);
            Settlement settlement = settlementService.getSettlement(member);

            Set<PermissionType> permissions = permissionService.getPermissions(role);
            if (settlement.isOwner(offlinePlayer) || permissions.contains(PermissionType.MEMBER_KICK_EXEMPT)) {
                player.sendMessage(Message.KICK_EXEMPT.prefix());
            } else {
                player.sendMessage(Message.KICK_PLAYER.prefix(offlinePlayer.getName()));
                Player target = offlinePlayer.getPlayer();
                if (target != null) target.sendMessage(Message.KICK.prefix(settlement.getName()));
                memberService.remove(offlinePlayer);
            }
            contents.inventory().close(player);
        }
    }
}