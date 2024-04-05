package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.enumeration.filters.MemberFilter;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.storage.types.Locale;
import com.huskydreaming.settlements.storage.types.Menu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MemberInventory implements InventoryProvider {

    private final HuskyPlugin plugin;
    private final ConfigService configService;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;
    private final OfflinePlayer offlinePlayer;

    public MemberInventory(HuskyPlugin plugin, OfflinePlayer offlinePlayer) {
        this.plugin = plugin;

        configService = plugin.provide(ConfigService.class);
        inventoryService = plugin.provide(InventoryService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);

        this.offlinePlayer = offlinePlayer;
    }


    @Override
    public void init(Player player, InventoryContents contents) {

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());

        contents.fillBorders(InventoryItem.border());
        contents.set(0, 0, InventoryItem.back(player, inventoryService.getMembersInventory(plugin, player, MemberFilter.ALL)));
        contents.set(1, 3, setOwner(player, settlement, contents));
        contents.set(1, 4, roleItem(player, settlement.getDefaultRole(), contents));
        contents.set(1, 5, kickItem(player, settlement, contents));

        Config config = configService.getConfig();
        if(config.isTeleportation() && offlinePlayer.isOnline() && offlinePlayer.getPlayer() != player) {
            contents.set(1, 1, teleportItem(player));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ClickableItem setOwner(Player player, Settlement settlement, InventoryContents contents) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.MEMBER_SET_OWNER_TITLE.parse())
                .setLore(Menu.MEMBER_SET_OWNER_LORE.parameterizeList(settlement.getOwnerName()))
                .setMaterial(Material.EMERALD)
                .build(), e -> {
            if (settlement.isOwner(player)) {

                if (offlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                    player.sendMessage(Locale.SETTLEMENT_IS_OWNER.prefix());
                } else {
                    Player target = offlinePlayer.getPlayer();
                    if (target != null) {
                        target.sendMessage(Locale.SETTLEMENT_OWNER.prefix());
                    }
                    player.sendMessage(Locale.SETTLEMENT_OWNER_TRANSFERRED.prefix(offlinePlayer.getName()));
                    settlement.setOwner(offlinePlayer);
                }
            } else {
                player.sendMessage(Locale.SETTLEMENT_NOT_OWNER_TRANSFER.prefix());
            }
            contents.inventory().close(player);
        });
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
                player.sendMessage(Locale.PLAYER_TELEPORT.prefix(offlinePlayer.getName()));
            }
        });
    }

    private ClickableItem roleItem(Player player, String defaultRole, InventoryContents contents) {
        Member member = memberService.getCitizen(offlinePlayer);
        int index = roleService.getIndex(member.getSettlement(), member);
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.MEMBER_SET_ROLE_TITLE.parse())
                .setLore(Menu.MEMBER_SET_ROLE_LORE.parameterizeList(index, member.getRole()))
                .setMaterial(Material.WRITABLE_BOOK)
                .build(), e -> {

            Role role = roleService.sync(member, defaultRole);
            if (e.isRightClick()) {
                if (roleService.demote(member.getSettlement(), role, member)) {
                    contents.inventory().open(player);
                }
            } else if (e.isLeftClick()) {
                if (roleService.promote(member.getSettlement(), role, member)) {
                    contents.inventory().open(player);
                }
            }
        });
    }

    private ClickableItem kickItem(Player player, Settlement settlement, InventoryContents contents) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.MEMBER_KICK_TITLE.parse())
                .setLore(Menu.MEMBER_KICK_LORE.parseList())
                .setMaterial(Material.ANVIL)
                .build(), e -> {
            Member member = memberService.getCitizen(offlinePlayer);
            Role role = roleService.getRole(member);

            if (settlement.isOwner(offlinePlayer) || role.hasPermission(RolePermission.MEMBER_KICK_EXEMPT)) {
                player.sendMessage(Locale.SETTLEMENT_KICK_EXEMPT.prefix());
            } else {
                player.sendMessage(Locale.SETTLEMENT_KICK_PLAYER.prefix(offlinePlayer.getName()));
                Player target = offlinePlayer.getPlayer();
                if (target != null) target.sendMessage(Locale.SETTLEMENT_KICK.prefix(member.getSettlement()));
                memberService.remove(offlinePlayer);
            }
            contents.inventory().close(player);
        });
    }
}