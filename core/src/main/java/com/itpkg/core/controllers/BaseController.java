package com.itpkg.core.controllers;

import com.itpkg.core.utils.JsonHelper;
import com.itpkg.core.web.widgets.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Created by flamen on 15-7-22.
 */
public class BaseController {

    RedirectView doShow(Message msg) {
        RedirectView rv = new RedirectView();
        rv.setUrl("/#/show?msg=" + jsonHelper.object2json(msg));
        return rv;
    }

    @Autowired
    JsonHelper jsonHelper;
}
