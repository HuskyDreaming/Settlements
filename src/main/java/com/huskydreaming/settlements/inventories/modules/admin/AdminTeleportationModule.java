package com.huskydreaming.settlements.inventories.modules.admin;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.Permission;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.registries.CommandRegistry;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.commands.subcommands.SetSpawnCommand;
import com.huskydreaming.settlements.commands.subcommands.SpawnCommand;
import com.huskydreaming.settlements.inventories.modules.general.SpawnModule;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.enumeration.types.ConfigType;
import com.huskydreaming.settlements.storage.persistence.Config;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AdminTeleportationModule implements InventoryModule {

    private final HuskyPlugin plugin;
    private final ConfigService configService;
    private final CommandRegistry commandRegistry;

    public AdminTeleportationModule(HuskyPlugin plugin) {
        this.plugin = plugin;

        this.configService = plugin.provide(ConfigService.class);
        this.commandRegistry = plugin.getCommandRegistry();
    }

    @Override
    public ItemStack itemStack(Player player) {
        Config config = configService.getConfig();
        ConfigType configType = ConfigType.TELEPORTATION;
        return InventoryItem.of(config.isTeleportation(), configType.toString(), configType.getDescription());
    }

    @Override
    public void run(InventoryClickEvent event, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            Config config = configService.getConfig();
            if (config.isTeleportation()) {
                config.setTeleportation(false);
                commandRegistry.remove(CommandLabel.SPAWN);
                commandRegistry.remove(CommandLabel.SET_SPAWN);
            } else {
                config.setTeleportation(true);
                commandRegistry.add(new SpawnCommand(plugin));
                commandRegistry.add(new SetSpawnCommand(plugin));
            }
            contents.inventory().open(player);
        }
    }
}