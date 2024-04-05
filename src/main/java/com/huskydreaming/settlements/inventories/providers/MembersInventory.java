package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.enumeration.filters.MemberFilter;
import com.huskydreaming.settlements.inventories.actions.UnTrustInventoryAction;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.TrustService;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.storage.types.Menu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Consumer;

import java.util.List;

public class MembersInventory extends InventoryPageProvider<OfflinePlayer> {

    private final HuskyPlugin plugin;
    private final ConfigService configService;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final TrustService trustService;

    private MemberFilter memberFilter;

    public MembersInventory(HuskyPlugin plugin, int rows, OfflinePlayer[] offlinePlayers) {
        super(rows, offlinePlayers);
        this.plugin = plugin;

        configService = plugin.provide(ConfigService.class);
        inventoryService = plugin.provide(InventoryService.class);
        memberService = plugin.provide(MemberService.class);
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
        if (memberService.hasSettlement(offlinePlayer)) {
            Member offlineMember = memberService.getCitizen(offlinePlayer);
            Member onlineMember = memberService.getCitizen(player);
            if (onlineMember.getSettlement().equalsIgnoreCase(offlineMember.getSettlement())) {
                return memberItem(index, offlinePlayer, offlineMember);
            }
        }

        return trustedItem(index, offlinePlayer);
    }

    private ItemStack trustedItem(int index, OfflinePlayer offlinePlayer) {
        return ItemBuilder.create()
                .setDisplayName(Menu.TRUSTED_TITLE.parameterize(index, offlinePlayer.getName()))
                .setLore(Menu.TRUSTED_LORE.parameterizeList())
                .buildPlayer(offlinePlayer);
    }

    private ItemStack memberItem(int index, OfflinePlayer offlinePlayer, Member member) {
        String online = Menu.MEMBERS_STATUS_ONLINE.parse();
        String offline = Menu.MEMBERS_STATUS_OFFLINE.parse();
        String status = offlinePlayer.isOnline() ? online : offline;
        String zone = ZoneId.systemDefault().getDisplayName(TextStyle.SHORT, Locale.getDefault());
        String lastOnline = (offlinePlayer.isOnline() ? "Now" : member.getLastOnline()) + " (" + zone + ")";

        return ItemBuilder.create()
                .setDisplayName(Menu.MEMBERS_TITLE.parameterize(index, offlinePlayer.getName()))
                .setLore(Menu.MEMBERS_LORE.parameterizeList(member.getRole(), status, lastOnline))
                .buildPlayer(offlinePlayer);
    }

    @Override
    public void run(InventoryClickEvent event, OfflinePlayer offlinePlayer, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            Member onlineMember = memberService.getCitizen(player);
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