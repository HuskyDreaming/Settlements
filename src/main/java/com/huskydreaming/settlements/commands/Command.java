package com.huskydreaming.settlements.commands;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface Command {
    CommandLabel label();
    String arguments() default "";
    String[] aliases() default {};
    boolean debug() default false;
}
