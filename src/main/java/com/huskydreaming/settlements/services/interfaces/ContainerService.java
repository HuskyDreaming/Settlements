package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.database.dao.ContainerDao;
import com.huskydreaming.settlements.database.entities.Container;
import com.huskydreaming.settlements.database.entities.Settlement;

public interface ContainerService extends Service {

    void addContainer(Container container);

    Container createDefaultContainer();

    void deleteContainer(Container container);

    Container getContainer(Settlement settlement);

    ContainerDao getDao();
}
