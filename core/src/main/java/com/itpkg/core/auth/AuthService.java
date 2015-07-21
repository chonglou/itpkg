package com.itpkg.core.auth;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flamen on 15-7-20.
 */
public interface AuthService {
    boolean match(HttpServletRequest request);

}
