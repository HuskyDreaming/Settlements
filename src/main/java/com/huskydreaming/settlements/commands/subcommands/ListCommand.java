package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.commands.CommandBase;
import com.huskydreaming.settlements.inventories.InventorySupplier;
import org.bukkit.entity.Player;

public class ListCommand extends CommandBase {

    public ListCommand() {
        super("list", "l");
    }

    @Override
    public void run(Settlements settlements, Player player, String[] strings) {
        InventorySupplier.getSettlementsInventory().open(player);
    }
}
