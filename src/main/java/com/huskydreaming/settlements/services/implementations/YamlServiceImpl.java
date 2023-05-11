package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.services.interfaces.YamlService;
import com.huskydreaming.settlements.storage.Yaml;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Menu;

public class YamlServiceImpl implements YamlService {

    @Override
    public void deserialize(SettlementPlugin plugin) {
        // Localization for general messages
        Yaml locale = new Yaml("localisation/locale");
        locale.load(plugin);
        Locale.setConfiguration(locale.getConfiguration());

        for (Locale message : Locale.values()) {
            locale.getConfiguration().set(message.toString(), message.parse());
        }
        locale.save();

        // Localization for menus
        Yaml menu = new Yaml("localisation/menu");
        menu.load(plugin);
        Menu.setConfiguration(menu.getConfiguration());

        for (Menu message : Menu.values()) {
            menu.getConfiguration().set(message.toString(), message.parseList() != null ? message.parseList() : message.parse());
        }
        menu.save();
    }
}