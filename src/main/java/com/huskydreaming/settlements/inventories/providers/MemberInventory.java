package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.Locale;
import com.huskydreaming.settlements.storage.Menu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MemberInventory implements InventoryProvider {

    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;
    private final OfflinePlayer offlinePlayer;

    private final String settlementName;

    public MemberInventory(HuskyPlugin plugin, String settlementName, OfflinePlayer offlinePlayer) {
        this.plugin = plugin;

        inventoryService = plugin.provide(InventoryService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);

        this.settlementName = settlementName;
        this.offlinePlayer = offlinePlayer;
    }


    @Override
    public void init(Player player, InventoryContents contents) {

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());

        contents.fillBorders(InventoryItem.border());
        contents.set(0, 0, InventoryItem.back(player, inventoryService.getCitizensInventory(plugin, settlementName)));
        contents.set(1, 3, setOwner(player, settlement, contents));
        contents.set(1, 4, roleItem(player, settlement.getDefaultRole(), contents));
        contents.set(1, 5, kickItem(player, settlement, contents));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ClickableItem setOwner(Player player, Settlement settlement, InventoryContents contents) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.MEMBER_SET_OWNER_TITLE.parse())
                .setLore(Menu.MEMBER_SET_OWNER_LORE.parseList())
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

    private ClickableItem roleItem(Player player, String defaultRole, InventoryContents contents) {
        Member member = memberService.getCitizen(offlinePlayer);
        int index = roleService.getIndex(settlementName, member);
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.MEMBER_SET_ROLE_TITLE.parse())
                .setLore(Menu.MEMBER_SET_ROLE_LORE.parameterizeList(index, member.getRole()))
                .setMaterial(Material.WRITABLE_BOOK)
                .build(), e -> {

            Role role = roleService.sync(member, defaultRole);
            if (e.isRightClick()) {
                if (roleService.demote(settlementName, role, member)) {
                    contents.inventory().open(player);
                }
            } else if (e.isLeftClick()) {
                if (roleService.promote(settlementName, role, member)) {
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
                if (target != null) target.sendMessage(Locale.SETTLEMENT_KICK.prefix(settlementName));
                memberService.remove(offlinePlayer);
            }
            contents.inventory().close(player);
        });
    }
}