package com.itpkg.core.auth;

import java.lang.annotation.*;

/**
 * Created by flamen on 15-7-20.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
}
