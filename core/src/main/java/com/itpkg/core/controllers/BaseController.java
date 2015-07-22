package com.itpkg.core.controllers;

import com.itpkg.core.models.Token;
import com.itpkg.core.utils.EncryptHelper;
import com.itpkg.core.utils.JsonHelper;
import com.itpkg.core.utils.JwtHelper;
import com.itpkg.core.web.widgets.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Created by flamen on 15-7-22.
 */
public class BaseController {

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
}
