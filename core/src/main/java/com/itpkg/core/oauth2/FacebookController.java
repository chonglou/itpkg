package com.itpkg.core.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by flamen on 15-7-15.
 */
@RestController
public class FacebookController {
    @RequestMapping(value = "/api/facebook/details", method = RequestMethod.GET)
    public User getSocialDetails() {
        return facebook.userOperations().getUserProfile();
    }

    @Autowired
    Facebook facebook;
}
