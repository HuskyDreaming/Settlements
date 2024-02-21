package com.huskydreaming.settlements.commands;

public enum CommandLabel {
    ACCEPT,
    ADMIN,
    AUTOCLAIM,
    CLAIM,
    CREATE,
    CREATEROLE,
    DELETEROLE,
    DENY,
    DISBAND,
    HELP,
    INVITE,
    KICK,
    LEAVE,
    LIST,
    SETDESCRIPTION,
    SETOWNER,
    SETSPAWN,
    SETTAG,
    SETTLEMENTS,
    SPAWN,
    TEST,
    UNCLAIM;

    public static boolean contains(String string) {
        for (CommandLabel c : CommandLabel.values()) {
            if (c.name().equals(string.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
