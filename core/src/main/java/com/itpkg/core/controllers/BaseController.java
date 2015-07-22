package com.itpkg.core.controllers;

import com.itpkg.core.models.Token;
import com.itpkg.core.services.I18nService;
import com.itpkg.core.utils.EmailHelper;
import com.itpkg.core.utils.EncryptHelper;
import com.itpkg.core.utils.JsonHelper;
import com.itpkg.core.utils.JwtHelper;
import com.itpkg.core.web.widgets.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;

/**
 * Created by flamen on 15-7-22.
 */
public class BaseController {

    void sendMail(String email, String url, Token token, Locale locale) {

        String code = encryptHelper.toBase64(jwtHelper.payload2token(url, token, 30));
        String subject = i18n.T("mail." + token.getAction() + ".subject");
        String body = i18n.T(
                "mail.user." + token.getAction() + ".body",
                email,
                String.format("%s%s/%s?locale=%s", home, url, code, locale)
        );
        emailHelper.send(email, subject, body);
    }

    Token toToken(String t) {
        return jwtHelper.token2payload(encryptHelper.fromBase64(t), Token.class);
    }

    RedirectView doShow(Message msg) {
        RedirectView rv = new RedirectView();
        //rv.setUrl("/#/show?msg=" + URLEncoder.encode(jsonHelper.object2json(msg), "UTF-8" ));
        rv.setUrl("/#/show?msg=" + encryptHelper.toBase64(jsonHelper.object2json(msg)));
        return rv;
    }

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
