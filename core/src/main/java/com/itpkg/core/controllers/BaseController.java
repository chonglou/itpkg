package com.itpkg.core.controllers;

import com.itpkg.core.auth.JwtHelper;
import com.itpkg.core.auth.UserToken;
import com.itpkg.core.services.I18nService;
import com.itpkg.core.services.SessionService;
import com.itpkg.core.utils.EmailHelper;
import com.itpkg.core.utils.EncryptHelper;
import com.itpkg.core.utils.JsonHelper;
import com.itpkg.core.web.widgets.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;

/**
 * Created by flamen on 15-7-22.
 */
public class BaseController {

    void releaseToken(String token) {
        sessionService.delByToken(encryptHelper.fromBase64(token));
    }

    void sendTokenMail(String email, String path, UserToken token, Locale locale) throws Exception {
        String code = token2string(path, token, 30);
        String subject = i18n.T("mail." + token.getAction() + ".subject");
        String body = i18n.T(
                "mail." + token.getAction() + ".body",
                email,
                String.format("%s%s/%s?locale=%s", home, path, code, locale)
        );
        emailHelper.send(email, subject, body);
    }

    String token2string(String subject, UserToken token, int minutes) throws Exception {
        return encryptHelper.toBase64(jwtHelper.payload2token(subject, token, minutes));
    }

    UserToken string2token(String token) throws Exception {
        return jwtHelper.token2payload(encryptHelper.fromBase64(token), UserToken.class);
    }

    RedirectView doShow(Message msg) {
        RedirectView rv = new RedirectView();
        rv.setUrl("/#/show?msg=" + encryptHelper.toBase64(jsonHelper.object2json(msg)));
        return rv;
    }

    @Autowired
    SessionService sessionService;
    @Autowired
    JsonHelper jsonHelper;
    @Autowired
    EncryptHelper encryptHelper;
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    I18nService i18n;
    @Autowired
    EmailHelper emailHelper;
    @Value("${http.home}")
    String home;
}
