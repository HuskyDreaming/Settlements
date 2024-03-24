package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.storage.parseables.DefaultMenu;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.persistence.Flag;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.services.interfaces.FlagService;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class FlagsInventory extends InventoryPageProvider<Flag> {

    private final FlagService flagService;
    private final MemberService memberService;

    public FlagsInventory(HuskyPlugin plugin, String name, int rows, Flag[] flags) {
        super(rows, flags);

        InventoryService inventoryService = plugin.provide(InventoryService.class);

        this.flagService = plugin.provide(FlagService.class);
        this.memberService = plugin.provide(MemberService.class);
        this.smartInventory = inventoryService.getSettlementInventory(plugin, name);
    }

    @Override
    public ItemStack construct(Player player, int i, Flag flag) {
        Member member = memberService.getCitizen(player);

        boolean enabled = flagService.hasFlag(member.getSettlement(), flag);
        String flagName = Util.capitalize(flag.name());

        String materialEnabled = DefaultMenu.ENABLE_MATERIAL.parse();
        String materialDisabled = DefaultMenu.DISABLED_MATERIAL.parse();

        String displayNameEnabled = DefaultMenu.ENABLE_TITLE.parameterize(flagName);
        String displayNameDisabled = DefaultMenu.DISABLED_TITLE.parameterize(flagName);

        return ItemBuilder.create()
                .setDisplayName(enabled ? displayNameEnabled : displayNameDisabled)
                .setLore(enabled ? DefaultMenu.DISABLED_DESCRIPTION.parse() : DefaultMenu.ENABLED_DESCRIPTION.parse())
                .setMaterial(Material.valueOf(enabled ? materialEnabled : materialDisabled))
                .build();
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