package com.itpkg.core.auth;

import com.itpkg.core.dao.RoleDao;
import com.itpkg.core.dao.UserDao;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by flamen on 15-7-20.
 */
@Aspect
public class RoleAspect {
    private static final Logger logger = LoggerFactory.getLogger(RoleAspect.class);

    @Around("within(com.itpkg.*.controllers.*) && @annotation(requestMapping) && @annotation(rule)")
    public Object ruleBean(ProceedingJoinPoint joinPoint, RequestMapping requestMapping, Rule rule) throws Exception {

        String rType = rule.resourceType() == Void.class ? null : rule.resourceType().getTypeName();

        //Long rId = rType == null || "nil".equals(rule.resourceId()) ? null : (Long)joinPoint.getArgs()[0];
        Long rId = null;
        if (rType != null && !"nil".equals(rule.resourceId())) {
            Object[] args = joinPoint.getArgs();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
            String[] names = methodSignature.getParameterNames();
            for (int i = 0; i < names.length; i++) {
                if (names[i].equals(rule.resourceId())) {
                    rId = (Long) args[i];
                }
            }
        }

        logger.debug(String.format("AUTH(%s, %s, %d) %s", rule.role(), rType, rId, request.getRequestURI()));


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
    UserDao userDao;
    @Autowired
    RoleDao roleDao;

}
