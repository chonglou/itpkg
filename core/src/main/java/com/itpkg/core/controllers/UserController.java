package com.itpkg.core.controllers;

import com.itpkg.core.services.I18nService;
import com.itpkg.core.services.UserService;
import com.itpkg.core.web.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by flamen on 15-7-14.
 */


@Controller
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/user/sign_in", method = RequestMethod.GET)
    @ResponseBody
    Form getSignIn() {
        Form fm = new Form("sign_in", i18n.T("form.user.sign_in.title"), "/user/sign_in");
        fm.addEmailField("email", i18n.T("form.fields.email"));
        fm.addPasswordField("password", i18n.T("form.fields.password"));
        fm.addSubmit(i18n.T("form.user.sign_in.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        return fm;
    }

    @RequestMapping(value = "/user/sign_up", method = RequestMethod.GET)
    @ResponseBody
    Form getSignUp() {
        Form fm = new Form("sign_up", i18n.T("form.user.sign_up.title"), "/user/sign_up");
        fm.addTextField("username", i18n.T("form.fields.username"));
        fm.addEmailField("email", i18n.T("form.fields.email"));
        fm.addPasswordField("password", i18n.T("form.fields.password"));
        fm.addPasswordField("password_confirm", i18n.T("form.fields.password_confirm"));
        fm.addSubmit(i18n.T("form.user.sign_up.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        return fm;
    }

    @RequestMapping(value = "/user/forgot_password", method = RequestMethod.GET)
    @ResponseBody
    Form getForgotPassword() {
        Form fm = new Form("forgot_password", i18n.T("form.user.forgot_password.title"), "/user/forgot_password");
        fm.addEmailField("email", i18n.T("form.fields.email"));
        fm.addSubmit(i18n.T("form.user.forgot_password.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        return fm;
    }

    @RequestMapping(value = "/user/change_password", method = RequestMethod.GET)
    @ResponseBody
    Form getChangePassword() {
        Form fm = new Form("change_password", i18n.T("form.user.change_password.title"), "/user/change_password");
        fm.addEmailField("email", i18n.T("form.fields.email"));
        fm.addSubmit(i18n.T("form.user.change_password.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        return fm;
    }

    @RequestMapping(value = "/user/confirm", method = RequestMethod.GET)
    @ResponseBody
    Form getConfirm() {
        Form fm = new Form("confirm", i18n.T("form.user.confirm.title"), "/user/confirm");
        fm.addEmailField("email", i18n.T("form.fields.email"));
        fm.addSubmit(i18n.T("form.user.confirm.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        return fm;
    }

    @RequestMapping(value = "/user/unlock", method = RequestMethod.GET)
    @ResponseBody
    Form getUnlock() {
        Form fm = new Form("unlock", i18n.T("form.user.unlock.title"), "/user/unlock");
        fm.addEmailField("email", i18n.T("form.fields.email"));
        fm.addSubmit(i18n.T("form.user.unlock.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        return fm;
    }


    @Autowired
    UserService userService;

    @Autowired
    I18nService i18n;
}
