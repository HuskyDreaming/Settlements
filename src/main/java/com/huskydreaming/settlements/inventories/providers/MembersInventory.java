package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.settlements.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import com.huskydreaming.settlements.utilities.Menu;
import com.huskydreaming.settlements.utilities.Remote;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MembersInventory extends InventoryPageProvider<OfflinePlayer> {

    private final InventoryService inventoryService;
    private final MemberService memberService;

    public MembersInventory(Settlement settlement, int rows, OfflinePlayer[] offlinePlayers) {
        super(settlement, rows, offlinePlayers);

        inventoryService = ServiceProvider.Provide(InventoryService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        this.smartInventory = inventoryService.getSettlementInventory(settlement);
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
                .setDisplayName(Remote.parameterize(Menu.MEMBERS_TITLE, index, offlinePlayer.getName()))
                .setLore(Remote.parameterizeList(Menu.MEMBERS_LORE, member.getRole(), status, lastOnline))
                .buildPlayer(offlinePlayer);
    }

    @Override
    public void run(InventoryClickEvent event, OfflinePlayer offlinePlayer, InventoryContents contents) {
        if(event.getWhoClicked() instanceof Player player) {
           inventoryService.getCitizenInventory(settlement, offlinePlayer).open(player);
        }
    }
}
