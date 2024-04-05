package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.enumeration.types.NotificationType;

public interface ConfigService extends Service {

    void selectNotificationType(NotificationType notificationType);

    Config getConfig();
}