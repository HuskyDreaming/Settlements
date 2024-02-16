package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.settlements.inventories.InventoryItem;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Menu;
import com.huskydreaming.settlements.utilities.Remote;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MemberInventory implements InventoryProvider {

    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final RoleService roleService;

    private final Settlement settlement;
    private final OfflinePlayer offlinePlayer;

    public MemberInventory(Settlement settlement, OfflinePlayer offlinePlayer) {
        inventoryService = ServiceProvider.Provide(InventoryService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        roleService = ServiceProvider.Provide(RoleService.class);

        this.settlement = settlement;
        this.offlinePlayer = offlinePlayer;
    }


    @Override
    public void init(Player player, InventoryContents contents) {

        contents.fillBorders(InventoryItem.border());
        contents.set(0, 0, InventoryItem.back(player, inventoryService.getCitizensInventory(settlement)));
        contents.set(1, 3, setOwner(player, contents));
        contents.set(1, 4, roleItem(player, contents));
        contents.set(1, 5, kickItem(player, contents));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ClickableItem setOwner(Player player, InventoryContents contents) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.MEMBER_SET_OWNER_TITLE.parse())
                .setLore(Menu.MEMBER_SET_OWNER_LORE.parseList())
                .setMaterial(Material.EMERALD)
                .build(), e-> {
            if(settlement.isOwner(player)) {

                if(offlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                    player.sendMessage(Remote.prefix(Locale.SETTLEMENT_IS_OWNER));
                } else {
                    Player target = offlinePlayer.getPlayer();
                    if (target != null) {
                        target.sendMessage(Remote.prefix(Locale.SETTLEMENT_OWNER));
                    }
                    player.sendMessage(Remote.prefix(Locale.SETTLEMENT_OWNER_TRANSFERRED, offlinePlayer.getName()));
                    settlement.setOwner(offlinePlayer);
                }
            } else {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_NOT_OWNER_TRANSFER));
            }
            contents.inventory().close(player);
        });
    }

    private ClickableItem roleItem(Player player, InventoryContents contents) {
        Member member = memberService.getCitizen(offlinePlayer);
        int index = roleService.getIndex(settlement, member);
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.MEMBER_SET_ROLE_TITLE.parse())
                .setLore(Remote.parameterizeList(Menu.MEMBER_SET_ROLE_LORE, index, member.getRole()))
                .setMaterial(Material.WRITABLE_BOOK)
                .build(), e-> {
            if(e.isRightClick()) {
                if(roleService.demote(settlement, member)) {
                    contents.inventory().open(player);
                }
            } else if(e.isLeftClick()) {
                if(roleService.promote(settlement, member)) {
                    contents.inventory().open(player);
                }
            }
        });
    }

    private ClickableItem kickItem(Player player, InventoryContents contents) {
        return ClickableItem.of(ItemBuilder.create()
                .setDisplayName(Menu.MEMBER_KICK_TITLE.parse())
                .setLore(Menu.MEMBER_KICK_LORE.parseList())
                .setMaterial(Material.ANVIL)
                .build(), e-> {
            Member member = memberService.getCitizen(offlinePlayer);
            Role role = roleService.getRole(settlement, member);

            if(settlement.isOwner(offlinePlayer) || role.hasPermission(RolePermission.MEMBER_KICK_EXEMPT)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_KICK_EXEMPT));
            } else {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_KICK_PLAYER, offlinePlayer.getName()));
                Player target = offlinePlayer.getPlayer();
                if(target != null) target.sendMessage(Remote.prefix(Locale.SETTLEMENT_KICK, settlement.getName()));
                memberService.remove(offlinePlayer);
            }
            contents.inventory().close(player);
        });
    }
}
