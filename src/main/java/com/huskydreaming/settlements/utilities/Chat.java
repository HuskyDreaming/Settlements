package com.huskydreaming.settlements.utilities;

import org.bukkit.ChatColor;

public class Chat {

    public static String parameterize(String string, String... strings) {
        for(int i = 0; i < strings.length; i++) {
            string = string.replace("{" + i + "}", strings[0]);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
