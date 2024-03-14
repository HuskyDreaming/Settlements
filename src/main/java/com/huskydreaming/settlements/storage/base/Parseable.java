package com.huskydreaming.settlements.storage.base;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public interface Parseable {

    String parse();

    List<String> parseList();

    default String parameterizeObjects(Object[] objects) {
        String string = parse();
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            String parameter = (object instanceof String stringObject) ? stringObject : String.valueOf(object);
            string = string.replace("{" + i + "}", parameter);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    default String parameterize(Object... objects) {
        String string = parse();
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            String parameter = (object instanceof String stringObject) ? stringObject : String.valueOf(object);
            string = string.replace("{" + i + "}", parameter);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    default List<String> parameterizeList(Object... objects) {
        List<String> parameterList = new ArrayList<>();
        for (String string : parseList()) {
            for (int i = 0; i < objects.length; i++) {
                string = string.replace("{" + i + "}", String.valueOf(objects[i]));
            }
            parameterList.add(string);
        }
        return parameterList;
    }
}