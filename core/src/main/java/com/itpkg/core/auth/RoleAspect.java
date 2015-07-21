package com.itpkg.core.auth;

import com.itpkg.core.errors.UserNotFoundException;
import com.itpkg.core.models.User;
import com.itpkg.core.services.I18nService;
import com.itpkg.core.services.RoleService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by flamen on 15-7-20.
 */
@Aspect
public class RoleAspect {
    private static final Logger logger = LoggerFactory.getLogger(RoleAspect.class);

    @Around("within(com.itpkg.*.controllers.*) && @annotation(requestMapping) && @annotation(rule)")
    public Object ruleBean(ProceedingJoinPoint joinPoint, RequestMapping requestMapping, Rule rule) throws Exception {

        String rType = Void.class.equals(rule.resourceType()) ? null : rule.resourceType().getTypeName();

        Long rId = null;
        User currentUser = null;
        if (rType != null && !"nil".equals(rule.resourceId())) {
            Object[] args = joinPoint.getArgs();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
            String[] names = methodSignature.getParameterNames();
            Method method = methodSignature.getMethod();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            assert args.length == parameterAnnotations.length && args.length == names.length;
            for (int i = 0; i < names.length; i++) {
                if (names[i].equals(rule.resourceId())) {
                    rId = (Long) args[i];
                    continue;
                }
                for (Annotation an : parameterAnnotations[i]) {
                    if (an instanceof CurrentUser) {
                        currentUser = (User) args[i];
                    }
                }
            }
        }

        logger.debug(String.format("AUTH(%s, %s, %d) %s", rule.role(), rType, rId, request.getRequestURI()));

        if (currentUser == null) {
            throw new UserNotFoundException(i18n.T("errors.user.not_sign_in"));
        }
        if (!currentUser.isConfirmed()) {
            throw new UserNotFoundException(i18n.T("errors.user.need_to_confirm"));
        }
        if (currentUser.isLocked()) {
            throw new UserNotFoundException(i18n.T("errors.user.need_to_unlock"));
        }

        if (!roleService.check(currentUser.getId(), rule.role(), rType, rId)) {
            throw new UserNotFoundException(i18n.T("errors.user.no_role"));
        }

        Object retVal;
        try {
            retVal = joinPoint.proceed();
        } catch (Throwable e) {
            throw new Exception(e);
        }

        return retVal;
    }

    @Autowired
    HttpServletRequest request;
    @Autowired
    RoleService roleService;

    @Autowired
    I18nService i18n;

}
