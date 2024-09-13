package com.huskydreaming.settlements.inventories.modules.admin;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.InventoryModule;
import com.huskydreaming.huskycore.registries.CommandRegistry;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.commands.subcommands.TrustCommand;
import com.huskydreaming.settlements.commands.subcommands.UnTrustCommand;
import com.huskydreaming.settlements.database.persistence.Config;
import com.huskydreaming.settlements.enumeration.types.ConfigType;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AdminTrustingModule implements InventoryModule {

    private final HuskyPlugin plugin;
    private final ConfigService configService;
    private final CommandRegistry commandRegistry;

    public AdminTrustingModule(HuskyPlugin plugin) {
        this.plugin = plugin;

        this.configService = plugin.provide(ConfigService.class);
        this.commandRegistry = plugin.getCommandRegistry();
    }

    @Override
    public ItemStack itemStack(Player player) {
        Config config = configService.getConfig();
        ConfigType configType = ConfigType.TRUSTING;
        return InventoryItem.of(config.isTrusting(), configType.toString(), configType.getDescription());
    }

    @Override
    public void run(InventoryClickEvent event, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            Config config = configService.getConfig();
            if (config.isTrusting()) {
                config.setTrusting(false);
                commandRegistry.remove(CommandLabel.TRUST);
                commandRegistry.remove(CommandLabel.UN_TRUST);
            } else {
                config.setTrusting(true);
                commandRegistry.add(new TrustCommand(plugin));
                commandRegistry.add(new UnTrustCommand(plugin));
            }
            contents.inventory().open(player);
        }
    }
}