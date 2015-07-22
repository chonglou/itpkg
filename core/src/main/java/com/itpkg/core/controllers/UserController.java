package com.itpkg.core.controllers;

import com.itpkg.core.forms.EmailFm;
import com.itpkg.core.forms.PasswordFm;
import com.itpkg.core.forms.SignInFm;
import com.itpkg.core.forms.SignUpFm;
import com.itpkg.core.models.Token;
import com.itpkg.core.models.User;
import com.itpkg.core.services.I18nService;
import com.itpkg.core.services.UserService;
import com.itpkg.core.utils.EmailHelper;
import com.itpkg.core.web.widgets.Form;
import com.itpkg.core.web.widgets.Link;
import com.itpkg.core.web.widgets.Message;
import com.itpkg.core.web.widgets.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.Locale;

/**
 * Created by flamen on 15-7-14.
 */


@Controller("core.controllers.users")
@RequestMapping("/users")
public class UserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


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
                Token ut = new Token();
                ut.setId(u.getId());
                ut.setAction(Token.Action.SIGN_IN);
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
                sendMail(u.getId(), fm.getEmail(), Token.Action.CONFIRM, locale);

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

                sendMail(u.getId(), fm.getEmail(), Token.Action.CHANGE_PASSWORD, locale);

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
            Token ut = toToken(fm.getToken());
            if (ut != null && Token.Action.CHANGE_PASSWORD == ut.getAction()) {
                userService.setPassword(ut.getId(), fm.getPassword());
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
                sendMail(u.getId(), u.getEmail(), Token.Action.CONFIRM, locale);
            }
        }
        return res;
    }

    @RequestMapping(value = "/confirm/{token}", method = RequestMethod.GET)
    RedirectView getConfirmToken(@PathVariable("token") String token) {

        Message msg;
        Token ut = toToken(token);
        if (ut != null && Token.Action.CONFIRM == ut.getAction()) {
            userService.setConfirmed(ut.getId());
            msg = Message.Success(i18n.T("logs.success"), null, new Link("users.sign_in", "form.user.sign_in.submit"));
        } else {
            msg = Message.Error(i18n.T("logs.failed"), i18n.T("errors.user.bad_token"), null);
        }
        return doShow(msg);
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
                sendMail(u.getId(), u.getEmail(), Token.Action.UNLOCK, locale);
            } else {
                res.addError(i18n.T("errors.user.bad_status"));
            }
        }
        return res;
    }

    @RequestMapping(value = "/unlock/{token}", method = RequestMethod.GET)
    RedirectView getUnlockToken(@PathVariable("token") String token) {
        logger.debug("GET TOKEN: " + token);
        Message msg;
        Token ut = toToken(token);
        if (ut != null && Token.Action.UNLOCK == ut.getAction()) {
            userService.setLocked(ut.getId(), null);
            msg = Message.Success(i18n.T("logs.success"), null, new Link("users.sign_in", "form.user.sign_in.submit"));
        } else {
            msg = Message.Error(i18n.T("logs.failed"), i18n.T("errors.user.bad_token"), null);
        }
        return doShow(msg);
    }


    private void sendMail(long uid, String email, Token.Action action, Locale locale) {
        String act = action.name().toLowerCase();
        Token ut = new Token();
        ut.setId(uid);
        ut.setAction(action);
        String token = encryptHelper.toBase64(jwtHelper.payload2token(act, ut, 30));
        String subject = i18n.T("mail.user." + act + ".subject");
        String body = i18n.T(
                "mail.user." + act + ".body",
                email,
                String.format("%s/users/%s/%s?locale=%s", home, act, token, locale)
        );
        emailHelper.send(email, subject, body);

    }

    @Autowired
    UserService userService;
    @Autowired
    I18nService i18n;
    @Autowired
    EmailHelper emailHelper;
    @Value("${http.home}")
    String home;
}
