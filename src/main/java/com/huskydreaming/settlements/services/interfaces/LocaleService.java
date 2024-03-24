package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.huskycore.storage.Yaml;

public interface LocaleService extends Service {

    Yaml getLocale();
    Yaml getMenu();
}