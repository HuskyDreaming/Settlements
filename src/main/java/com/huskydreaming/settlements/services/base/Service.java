package com.huskydreaming.settlements.services.base;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface Service {
    ServiceType type();
}
