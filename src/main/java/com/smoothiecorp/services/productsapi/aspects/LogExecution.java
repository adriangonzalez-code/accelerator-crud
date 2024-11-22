package com.smoothiecorp.services.productsapi.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecution {

    String message() default "Execution";

    boolean logReturn() default false;
    boolean asJson() default false;

    String logReturnProperty() default "";
    String asJsonProperty() default "";
}
