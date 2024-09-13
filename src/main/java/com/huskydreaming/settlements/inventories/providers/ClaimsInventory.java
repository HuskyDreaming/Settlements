package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import com.huskydreaming.settlements.database.entities.Claim;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Message;
import com.huskydreaming.settlements.enumeration.locale.Menu;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class ClaimsInventory extends InventoryPageProvider<Claim> {

    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;
    private final MemberService memberService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    private final boolean teleportation;

    public ClaimsInventory(HuskyPlugin plugin, int rows, Claim[] claims) {
        super(rows, claims);
        this.plugin = plugin;

        ConfigService configService = plugin.provide(ConfigService.class);
        inventoryService = plugin.provide(InventoryService.class);
        memberService = plugin.provide(MemberService.class);
        permissionService = plugin.provide(PermissionService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
        teleportation = configService.getConfig().isTeleportation();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        contents.set(0, 0, InventoryItem.back(player, inventoryService.getMainInventory(plugin, player)));
    }

    @Override
    public ItemStack construct(Player player, int i, Claim claim) {
        ItemBuilder builder = ItemBuilder.create()
                .setDisplayName(Menu.CLAIMS_TITLE.parameterize(claim.getX(), claim.getZ()))
                .setMaterial(Material.GRASS_BLOCK);

        Member member = memberService.getMember(player);
        Role role = roleService.getRole(member);
        Settlement settlement = settlementService.getSettlement(member);
        World world = Bukkit.getWorld(claim.getWorldUID());

        Set<PermissionType> permissions = permissionService.getPermissions(role);
        if (world != null && teleportation && (permissions.contains(PermissionType.CLAIM_TELEPORT) || settlement.isOwner(player))) {
            builder.setLore(Menu.CLAIMS_LORE.parameterizeList(world.getName()));
        }

        return builder.build();
    }

    @Override
    public void run(InventoryClickEvent event, Claim claim, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            if (event.isLeftClick() && teleportation) {
                Chunk chunk = claim.toChunk();
                World world = chunk.getWorld();
                int x = (chunk.getX() << 4) + 8;
                int z = (chunk.getZ() << 4) + 8;
                int y = world.getHighestBlockYAt(x, z, HeightMap.MOTION_BLOCKING_NO_LEAVES);

                Location location = new Location(world, x, y + 1, z);
                player.teleport(location);
                player.sendMessage(Message.GENERAL_TELEPORT.prefix(x, z));
            }
        }
    }
}