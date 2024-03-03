package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.services.base.ServiceInterface;
import com.huskydreaming.settlements.storage.types.Yaml;

public interface LocaleService extends ServiceInterface {

    Yaml getLocale();
    Yaml getMenu();
}