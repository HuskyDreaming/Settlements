package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.settlements.enumeration.Flag;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.services.interfaces.FlagService;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class FlagsInventory extends InventoryPageProvider<Flag> {

    private final FlagService flagService;
    private final HuskyPlugin plugin;
    private final MemberService memberService;
    private final InventoryService inventoryService;

    public FlagsInventory(HuskyPlugin plugin, int rows, Flag[] flags) {
        super(rows, flags);
        this.plugin = plugin;

        this.flagService = plugin.provide(FlagService.class);
        this.inventoryService = plugin.provide(InventoryService.class);
        this.memberService = plugin.provide(MemberService.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        contents.set(0, 0, InventoryItem.back(player, inventoryService.getMainInventory(plugin, player)));
    }

    @Override
    public ItemStack construct(Player player, int i, Flag flag) {
        Member member = memberService.getCitizen(player);
        boolean enabled = flagService.hasFlag(member.getSettlement(), flag);
        return InventoryItem.of(enabled, flag.toString(), flag.getDescription());
    }

    @Override
    public void run(InventoryClickEvent event, Flag flag, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            Member member = memberService.getCitizen(player);
            boolean enabled = flagService.hasFlag(member.getSettlement(), flag);
            if (enabled) {
                flagService.removeFlag(member.getSettlement(), flag);
            } else {
                flagService.addFlag(member.getSettlement(), flag);
            }
            contents.inventory().open(player);
        }
    }
}