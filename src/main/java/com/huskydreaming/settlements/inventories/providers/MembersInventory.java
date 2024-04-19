package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.enumeration.filters.MemberFilter;
import com.huskydreaming.settlements.inventories.actions.UnTrustInventoryAction;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.types.Menu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Consumer;

import java.util.List;

public class MembersInventory extends InventoryPageProvider<OfflinePlayer> {

    private final HuskyPlugin plugin;
    private final ConfigService configService;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;
    private final TrustService trustService;

    private MemberFilter memberFilter;

    public MembersInventory(HuskyPlugin plugin, int rows, OfflinePlayer[] offlinePlayers) {
        super(rows, offlinePlayers);
        this.plugin = plugin;

        configService = plugin.provide(ConfigService.class);
        inventoryService = plugin.provide(InventoryService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
        trustService = plugin.provide(TrustService.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        contents.set(0, 0, InventoryItem.back(player, inventoryService.getMainInventory(plugin, player)));

        Member member = memberService.getCitizen(player);
        List<OfflinePlayer> offlinePlayers = trustService.getOfflinePlayers(member.getSettlement());
        if (!configService.getConfig().isTrusting() || offlinePlayers.isEmpty()) return;

        ChatColor chatColor = switch (memberFilter) {
            case TRUSTED -> ChatColor.YELLOW;
            case MEMBER -> ChatColor.AQUA;
            case ALL -> ChatColor.GREEN;
        };

        ItemStack itemStack = InventoryItem.of(chatColor, memberFilter.name());
        Consumer<InventoryClickEvent> consumer = e -> {
            MemberFilter newMemberFilter = switch (memberFilter) {
                case ALL -> MemberFilter.MEMBER;
                case MEMBER -> MemberFilter.TRUSTED;
                case TRUSTED -> MemberFilter.ALL;
            };
            inventoryService.getMembersInventory(plugin, player, newMemberFilter).open(player);
        };

        contents.set(0, 1, ClickableItem.of(itemStack, consumer));
    }

    @Override
    public ItemStack construct(Player player, int index, OfflinePlayer offlinePlayer) {
        if (!memberService.hasSettlement(player)) {
            player.closeInventory();
            return null;
        }

        Member member = memberService.getCitizen(player);
        Role role = roleService.getRole(member);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());

        if (memberService.hasSettlement(offlinePlayer)) {
            Member offlineMember = memberService.getCitizen(offlinePlayer);
            Member onlineMember = memberService.getCitizen(player);
            if (onlineMember.getSettlement().equalsIgnoreCase(offlineMember.getSettlement())) {
                boolean editable = role.hasPermission(RolePermission.EDIT_MEMBERS) || settlement.isOwner(player);
                return memberItem(offlinePlayer, offlineMember, editable, index);
            }
        }

        boolean permission = role.hasPermission(RolePermission.MEMBER_TRUST) || settlement.isOwner(player);
        return trustedItem(offlinePlayer, permission, index);
    }

    private ItemStack trustedItem(OfflinePlayer offlinePlayer, boolean permission, int index) {
        List<String> lore = Menu.TRUSTED_LORE.parameterizeList();
        ItemBuilder itemBuilder = ItemBuilder.create()
                .setDisplayName(Menu.TRUSTED_TITLE.parameterize(index, offlinePlayer.getName()));

        if (permission) {
            lore.add("");
            lore.add(Menu.TRUSTED_LORE_REMOVE.parse());
        }

        return itemBuilder.build();
    }

    private ItemStack memberItem(OfflinePlayer offlinePlayer, Member member, boolean editable, int index) {
        String online = Menu.MEMBERS_STATUS_ONLINE.parse();
        String offline = Menu.MEMBERS_STATUS_OFFLINE.parse();
        String status = offlinePlayer.isOnline() ? online : offline;
        String zone = ZoneId.systemDefault().getDisplayName(TextStyle.SHORT, Locale.getDefault());
        String lastOnline = (offlinePlayer.isOnline() ? "Now" : member.getLastOnline()) + " (" + zone + ")";

        List<String> lore = Menu.MEMBERS_LORE.parameterizeList(member.getRole(), status, lastOnline);
        if (editable) {
            lore.add("");
            lore.add(Menu.MEMBERS_LORE_EDIT.parse());
        }

        return ItemBuilder.create()
                .setDisplayName(Menu.MEMBERS_TITLE.parameterize(index, offlinePlayer.getName()))
                .setLore(lore)
                .buildPlayer(offlinePlayer);
    }

    @Override
    public void run(InventoryClickEvent event, OfflinePlayer offlinePlayer, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if (!memberService.hasSettlement(player)) return;

            Member onlineMember = memberService.getCitizen(player);
            Role role = roleService.getRole(onlineMember);
            Settlement settlement = settlementService.getSettlement(onlineMember.getSettlement());

            if (!(role.hasPermission(RolePermission.EDIT_MEMBERS) || settlement.isOwner(player))) return;

            if (memberService.hasSettlement(offlinePlayer)) {
                Member offlineMember = memberService.getCitizen(offlinePlayer);
                if (offlineMember.getSettlement().equalsIgnoreCase(onlineMember.getSettlement())) {
                    inventoryService.getMemberInventory(plugin, offlinePlayer).open(player);
                    return;
                }
            }

            inventoryService.addAction(player, new UnTrustInventoryAction(plugin, onlineMember.getSettlement(), offlinePlayer));
            inventoryService.getConfirmationInventory(plugin, player).open(player);
        }
    }

    public void setMemberFilter(MemberFilter memberFilter) {
        this.memberFilter = memberFilter;
    }
}