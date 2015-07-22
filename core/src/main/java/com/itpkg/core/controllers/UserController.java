package com.itpkg.core.controllers;

import com.itpkg.core.forms.EmailFm;
import com.itpkg.core.forms.PasswordFm;
import com.itpkg.core.forms.SignInFm;
import com.itpkg.core.forms.SignUpFm;
import com.itpkg.core.models.User;
import com.itpkg.core.services.I18nService;
import com.itpkg.core.services.UserService;
import com.itpkg.core.utils.EmailHelper;
import com.itpkg.core.utils.JwtHelper;
import com.itpkg.core.web.widgets.Form;
import com.itpkg.core.web.widgets.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

/**
 * Created by flamen on 15-7-14.
 */


@Controller("core.controllers.users")
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public class UserToken {
        public long id;
        public String action;
    }


    @RequestMapping(value = "/sign_in", method = RequestMethod.GET)
    @ResponseBody
    Form getSignIn() {
        Form fm = new Form("sign_in", i18n.T("form.user.sign_in.title"), "/users/sign_in");
        fm.addEmailField("email", i18n.T("form.fields.email"), true);
        fm.addPasswordField("password", i18n.T("form.fields.password"), true, i18n.T("form.placeholders.password"));
        fm.addSubmit(i18n.T("form.user.sign_in.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/sign_in", method = RequestMethod.POST)
    @ResponseBody
    Response postSignIn(@RequestBody @Valid SignInFm fm, BindingResult result) {

        Response res = new Response(result);
        if (res.isOk()) {
            User u = userService.auth(fm.getEmail(), fm.getPassword());
            if (u == null) {
                res.addError(i18n.T("form.user.sign_in.failed"));
            } else {
                UserToken ut = new UserToken();
                ut.id = u.getId();
                res.addData(jwtHelper.payload2token("Sign in", ut, 60 * 24));
            }
        }
        return res;
    }

    @RequestMapping(value = "/sign_up", method = RequestMethod.GET)
    @ResponseBody
    Form getSignUp() {
        Form fm = new Form("sign_up", i18n.T("form.user.sign_up.title"), "/users/sign_up");
        fm.addTextField("username", i18n.T("form.fields.username"), null, 4, true, i18n.T("form.placeholders.username"));
        fm.addEmailField("email", i18n.T("form.fields.email"), true, i18n.T("form.placeholders.email"));
        fm.addPasswordField("password", i18n.T("form.fields.password"), true, i18n.T("form.placeholders.password"));
        fm.addPasswordField("passwordConfirm", i18n.T("form.fields.password_confirm"), true, i18n.T("form.placeholders.password_confirm"));
        fm.addSubmit(i18n.T("form.user.sign_up.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/sign_up", method = RequestMethod.POST)
    @ResponseBody
    Response postSignUp(@RequestBody @Valid SignUpFm fm, BindingResult result, Locale locale) {
        Response res = new Response(result);
        if (res.isOk()) {
            User u = userService.create(fm.getUsername(), fm.getEmail(), fm.getPassword());
            if (u == null) {
                res.addError(i18n.T("form.user.sign_up.failed"));
            } else {
                sendMail(u.getId(), fm.getEmail(), "confirm", locale);

            }
        }
        return res;
    }


    @RequestMapping(value = "/forgot_password", method = RequestMethod.GET)
    @ResponseBody
    Form getForgotPassword() {
        Form fm = new Form("forgot_password", i18n.T("form.user.forgot_password.title"), "/users/forgot_password");
        fm.addEmailField("email", i18n.T("form.fields.email"), true);
        fm.addSubmit(i18n.T("form.user.forgot_password.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/forgot_password", method = RequestMethod.POST)
    @ResponseBody
    Response postForgotPassword(@RequestBody @Valid SignUpFm fm, BindingResult result, Locale locale) {
        Response res = new Response(result);
        if (res.isOk()) {
            User u = userService.findByEmail(fm.getEmail());
            if (u == null) {
                res.addError(i18n.T("form.user.email_not_exists"));
            } else {

                sendMail(u.getId(), fm.getEmail(), "Change password", locale);

            }
        }
        return res;
    }

    @RequestMapping(value = "/change_password/{token}", method = RequestMethod.GET)
    @ResponseBody
    Form getChangePassword(@PathVariable("token") String token) {
        Form fm = new Form("change_password", i18n.T("form.user.change_password.title"), "/users/change_password");
        fm.addHidden("token", token);
        fm.addSubmit(i18n.T("form.user.change_password.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);

        return fm;
    }

    @RequestMapping(value = "/change_password", method = RequestMethod.POST)
    @ResponseBody
    Response postChangePassword(@RequestBody @Valid PasswordFm fm, BindingResult result) {

        Response res = new Response(result);
        if (res.isOk()) {
            UserToken ut = jwtHelper.token2payload(fm.getToken(), UserToken.class);
            if (ut != null && "forgot_password".equals(ut.action)) {
                userService.setPassword(ut.id, fm.getPassword());
            } else {
                res.addError(i18n.T("errors.user.bad_token"));
            }
        }
        return res;
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    @ResponseBody
    Form getConfirm() {
        Form fm = new Form("confirm", i18n.T("form.user.confirm.title"), "/users/confirm");
        fm.addEmailField("email", i18n.T("form.fields.email"), true);
        fm.addSubmit(i18n.T("form.user.confirm.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    @ResponseBody
    Response postConfirm(@RequestBody @Valid EmailFm fm, BindingResult result, Locale locale) {
        Response res = new Response(result);
        if (res.isOk()) {
            User u = userService.findByEmail(fm.getEmail());
            if (u == null || u.isConfirmed()) {
                res.addError(i18n.T("errors.user.not_found"));
            } else {
                sendMail(u.getId(), u.getEmail(), "confirm", locale);
            }
        }
        return res;
    }

    @RequestMapping(value = "/confirm/{token}", method = RequestMethod.GET)
    @ResponseBody
    Response getConfirmToken(@PathVariable("token") String token) {
        Response res = new Response();
        UserToken ut = jwtHelper.token2payload(token, UserToken.class);
        if (ut != null && "confirm".equals(ut.action)) {
            userService.setConfirmed(ut.id);
            res.setOk(true);
        } else {
            res.addError(i18n.T("errors.user.bad_status"));

        }
        return res;
    }


    @RequestMapping(value = "/unlock", method = RequestMethod.GET)
    @ResponseBody
    Form getUnlock() {
        Form fm = new Form("unlock", i18n.T("form.user.unlock.title"), "/users/unlock");
        fm.addEmailField("email", i18n.T("form.fields.email"), true);
        fm.addSubmit(i18n.T("form.user.unlock.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/unlock", method = RequestMethod.POST)
    @ResponseBody
    Response postUnlock(@RequestBody @Valid EmailFm fm, BindingResult result, Locale locale) {
        Response res = new Response(result);
        if (res.isOk()) {
            User u = userService.findByEmail(fm.getEmail());
            if (u != null && u.isLocked()) {
                sendMail(u.getId(), u.getEmail(), "unlock", locale);
            } else {
                res.addError(i18n.T("errors.user.bad_status"));
            }
        }
        return res;
    }

    @RequestMapping(value = "/unlock/{token}", method = RequestMethod.GET)
    @ResponseBody
    Response getUnlockToken(@PathVariable("token") String token) {
        Response res = new Response();
        UserToken ut = jwtHelper.token2payload(token, UserToken.class);
        if (ut != null && "unlock".equals(ut.action)) {
            userService.setLocked(ut.id, null);
            res.setOk(true);
        } else {
            res.addError(i18n.T("errors.user.bad_token"));

        }
        return res;
    }


    private void sendMail(long uid, String email, String action, Locale locale) {

        UserToken ut = new UserToken();
        ut.id = uid;
        ut.action = action;
        String token = jwtHelper.payload2token(action, ut, 30);
        String subject = i18n.T("mail.user." + action.toLowerCase() + ".subject");
        String body = i18n.T(
                "mail.user." + action.toLowerCase() + ".body",
                email,
                String.format("https://%s/users/%s/%s?locale=%s", i18n.T("site.domain"), action, token, locale)
        );
        emailHelper.send(email, subject, body);

    }

    @Autowired
    UserService userService;

    @Autowired
    I18nService i18n;
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    EmailHelper emailHelper;
}
