package com.itpkg.core.auth;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * Created by flamen on 15-7-20.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Rule {
     String role();
     Class<?> resourceType() default Void.class;
     String resourceId() default "nil";
}
