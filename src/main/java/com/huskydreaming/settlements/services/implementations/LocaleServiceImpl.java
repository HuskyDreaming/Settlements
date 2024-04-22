package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.Yaml;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.LocaleService;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.storage.types.Message;
import com.huskydreaming.settlements.storage.types.Menu;


public class LocaleServiceImpl implements LocaleService {

    private Yaml messages;
    private Yaml menus;

    public LocaleServiceImpl(HuskyPlugin plugin) {

        ConfigService configService = plugin.provide(ConfigService.class);
        Config config = configService.getConfig();

        loadMessages(plugin, config.getLocalization());
        loadMenus(plugin, config.getLocalization());
    }

    @Override
    public void loadMessages(HuskyPlugin plugin, String locale) {
        messages = new Yaml("localisation/messages_en");
        messages.load(plugin);
        Message.setConfiguration(messages.getConfiguration());

        for (Message message : Message.values()) {
            messages.getConfiguration().set(message.toString(), message.parseList() != null ? message.parseList() : message.parse());
        }
        messages.save();
    }

    @Override
    public void loadMenus(HuskyPlugin plugin, String language) {
        menus = new Yaml("menus/menu_en");
        menus.load(plugin);
        Menu.setConfiguration(menus.getConfiguration());

        for (Menu menu : Menu.values()) {
            menus.getConfiguration().set(menu.toString(), menu.parseList() != null ? menu.parseList() : menu.parse());
        }
        menus.save();
    }

    @Override
    public Yaml getMessages() {
        return messages;
    }

    @Override
    public Yaml getMenus() {
        return menus;
    }
}