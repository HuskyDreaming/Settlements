package com.huskydreaming.settlements.utilities;

import org.bukkit.ChatColor;

public class Chat {

    public static String parameterize(Locale locale, String... strings) {
        String string = locale.parse();
        for(int i = 0; i < strings.length; i++) {
            string = string.replace("{" + i + "}", strings[0]);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
