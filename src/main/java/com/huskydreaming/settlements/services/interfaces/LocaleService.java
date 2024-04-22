package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.huskycore.storage.Yaml;

import java.io.FilenameFilter;
import java.util.Set;

public interface LocaleService extends Service {

    void loadMessages(HuskyPlugin plugin, String locale);

    void loadMenus(HuskyPlugin plugin, String language);

    Yaml getMessages();

    Yaml getMenus();
}