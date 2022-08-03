package com.huskydreaming.settlements.modules;

import com.google.inject.AbstractModule;
import com.huskydreaming.settlements.services.*;
import com.huskydreaming.settlements.services.implementations.*;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CitizenService.class).to(CitizenServiceImpl.class);
        bind(ClaimService.class).to(ClaimServiceImpl.class);
        bind(DependencyService.class).to(DependencyServiceImpl.class);
        bind(InvitationService.class).to(InvitationServiceImpl.class);
        bind(RequestService.class).to(RequestServiceImpl.class);
        bind(SettlementService.class).to(SettlementServiceImpl.class);
        bind(YamlService.class).to(YamlServiceImpl.class);
    }
}
