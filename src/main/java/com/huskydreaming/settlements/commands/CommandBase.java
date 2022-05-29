package com.huskydreaming.settlements.commands;

import com.huskydreaming.settlements.Settlements;
import org.bukkit.entity.Player;

public abstract class CommandBase {

    protected final String name;
    protected final String[] aliases;

    protected CommandBase(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public abstract void run(Settlements settlements, Player player, String[] strings);
}
