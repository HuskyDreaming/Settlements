package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import com.huskydreaming.settlements.storage.enumerations.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;

public class MembersInventory extends InventoryPageProvider<OfflinePlayer> {

    private final SettlementPlugin plugin;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final String settlementName;

    public MembersInventory(SettlementPlugin plugin, String settlementName, int rows, OfflinePlayer[] offlinePlayers) {
        super(rows, offlinePlayers);
        this.plugin = plugin;

        inventoryService = plugin.provide(InventoryService.class);
        memberService = plugin.provide(MemberService.class);
        this.settlementName = settlementName;
        this.smartInventory = inventoryService.getSettlementInventory(plugin, settlementName);
    }

    @Override
    public ItemStack construct(int index, OfflinePlayer offlinePlayer) {
        Member member = memberService.getCitizen(offlinePlayer);

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
            inventoryService.getCitizenInventory(plugin, settlementName, offlinePlayer).open(player);
        }
    }
}