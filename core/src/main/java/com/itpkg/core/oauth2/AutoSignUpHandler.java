package com.itpkg.core.oauth2;

import com.itpkg.core.models.Log;
import com.itpkg.core.models.User;
import com.itpkg.core.services.I18nService;
import com.itpkg.core.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * Created by flamen on 15-7-15.
 */
@Component
public class AutoSignUpHandler implements ConnectionSignUp {
    @Override
    public String execute(Connection<?> c) {
        User user = userService.create(c.fetchUserProfile(), c.getKey(), c.createData());
        userService.log(user, i18n.T("log.user.sign_up.success"), Log.Type.INFO);
        return user.getId();
    }

    @Autowired
    UserService userService;
    @Autowired
    I18nService i18n;

}
