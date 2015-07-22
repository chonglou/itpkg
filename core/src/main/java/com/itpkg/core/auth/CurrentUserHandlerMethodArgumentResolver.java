package com.itpkg.core.auth;

import com.itpkg.core.controllers.UserController;
import com.itpkg.core.models.User;
import com.itpkg.core.services.UserService;
import com.itpkg.core.utils.JwtHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by flamen on 15-7-20.
 */
public class CurrentUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger logger = LoggerFactory.getLogger(CurrentUserHandlerMethodArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null && parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String auth = webRequest.getHeader("Authorization");
        if (auth != null) {
            String[] ss = auth.split(" ");
            if (ss.length == 2 && !"Bearer".equals(ss[0])) {
                UserController.UserToken token;
                try {
                    token = jwtHelper.token2payload(ss[1], UserController.UserToken.class);
                    return userService.findById(token.id);
                } catch (Exception e) {
                    logger.error("Bad token", e);
                }
            }
        }
        return null;//WebArgumentResolver.UNRESOLVED;
    }

    @Autowired
    UserService userService;
    @Autowired
    JwtHelper jwtHelper;

}
