package com.huskydreaming.settlements.commands;

import org.bukkit.entity.Player;

public interface CommandInterface {

    default CommandLabel getLabel() {
        return getClass().getAnnotation(Command.class).label();
    }

    default String[] getAliases() {
        return getClass().getAnnotation(Command.class).aliases();
    }

    void run(Player player, String[] strings);
}
