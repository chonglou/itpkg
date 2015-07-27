package com.itpkg.core.controllers;

import com.itpkg.core.forms.EmailFm;
import com.itpkg.core.forms.PasswordFm;
import com.itpkg.core.forms.SignInFm;
import com.itpkg.core.forms.SignUpFm;
import com.itpkg.core.models.Token;
import com.itpkg.core.models.User;
import com.itpkg.core.services.UserService;
import com.itpkg.core.web.widgets.Form;
import com.itpkg.core.web.widgets.Link;
import com.itpkg.core.web.widgets.Message;
import com.itpkg.core.web.widgets.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        fm.addEmailField("email", i18n.T("form.fields.email"), true, i18n.T("form.placeholders.email"));
        fm.addPasswordField("password", i18n.T("form.fields.password"), true, i18n.T("form.placeholders.password"));
        fm.addSubmit(i18n.T("form.user.sign_in.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/sign_in", method = RequestMethod.POST)
    @ResponseBody
    Response postSignIn(@RequestBody @Valid SignInFm fm, BindingResult result) throws Exception{

        Response res = new Response(result);
        if (res.isOk()) {
            User u = userService.auth(fm.getEmail(), fm.getPassword());
            if (u == null) {
                res.addError(i18n.T("form.user.sign_in.failed"));
            } else {
                if (!u.isConfirmed()) {
                    res.addError(i18n.T("errors.user.need_to_confirm"));
                } else if (u.isLocked()) {
                    res.addError(i18n.T("errors.user.need_to_unlock"));
                } else {
                    Token ut = new Token();
                    ut.setUid(u.getId());
                    res.addData(jwtHelper.payload2token("/users/sign_in", new Token(u.getId(), "users.sign_in"), 60 * 24));
                }
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
    Response postSignUp(@RequestBody @Valid SignUpFm fm, BindingResult result, Locale locale) throws Exception{
        Response res = new Response(result);
        if (res.isOk()) {
            User u = userService.create(fm.getUsername(), fm.getEmail(), fm.getPassword());
            if (u == null) {
                res.addError(i18n.T("form.user.sign_up.failed"));
            } else {
                sendTokenMail(fm.getEmail(), "/users/confirm", new Token(u.getId(), "users.confirm"), locale);
                res.addData(i18n.T("form.user.confirm.success"));
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
    Response postForgotPassword(@RequestBody @Valid EmailFm fm, BindingResult result, Locale locale)  throws Exception{
        Response res = new Response(result);
        if (res.isOk()) {
            User u = userService.findByEmail(fm.getEmail());
            if (u == null) {
                res.addError(i18n.T("form.user.email_not_exists"));
            } else {
                Token token = new Token(u.getId(), "users.change_password");
                String code = token2string("/users/change_password", token, 30);
                String subject = i18n.T("mail." + token.getAction() + ".subject");
                String body = i18n.T(
                        "mail." + token.getAction() + ".body",
                        u.getEmail(),
                        String.format("%s/#/users/change_password?code=%s&locale=%s", home, code, locale)
                );
                emailHelper.send(u.getEmail(), subject, body);
                res.addData(i18n.T("form.user.forgot_password.success"));
            }
        }
        return res;
    }

    @RequestMapping(value = "/change_password/{token}", method = RequestMethod.GET)
    @ResponseBody
    Form getChangePassword(@PathVariable("token") String token) {
        Form fm = new Form("change_password", i18n.T("form.user.change_password.title"), "/users/change_password");
        fm.addHidden("token", token);
        fm.addPasswordField("password", i18n.T("form.fields.password"), true, i18n.T("form.placeholders.password"));
        fm.addPasswordField("passwordConfirm", i18n.T("form.fields.password_confirm"), true, i18n.T("form.placeholders.password_confirm"));
        fm.addSubmit(i18n.T("form.user.change_password.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);

        return fm;
    }

    @RequestMapping(value = "/change_password", method = RequestMethod.POST)
    @ResponseBody
    Response postChangePassword(@RequestBody @Valid PasswordFm fm, BindingResult result)  throws Exception{

        Response res = new Response(result);
        if (res.isOk()) {
            Token ut = string2token(fm.getToken());
            if (ut != null && "users.change_password".equals(ut.getAction())) {
                userService.setPassword(ut.getUid(), fm.getPassword());
                releaseToken(fm.getToken());
                res.addData(i18n.T("form.user.change_password.success"));
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
    Response postConfirm(@RequestBody @Valid EmailFm fm, BindingResult result, Locale locale)  throws Exception{
        Response res = new Response(result);
        if (res.isOk()) {
            User u = userService.findByEmail(fm.getEmail());
            if (u == null) {
                res.addError(i18n.T("errors.user.not_found"));
            } else {
                if (u.isConfirmed()) {
                    res.addError(i18n.T("errors.user.bad_status"));
                } else {
                    sendTokenMail(fm.getEmail(), "/users/confirm", new Token(u.getId(), "users.confirm"), locale);
                    res.addData(i18n.T("form.user.confirm.success"));
                }
            }
        }
        return res;
    }

    @RequestMapping(value = "/confirm/{token}", method = RequestMethod.GET)
    RedirectView getConfirmToken(@PathVariable("token") String token)  throws Exception{

        Message msg;
        Token ut = string2token(token);
        if (ut != null && "users.confirm".equals(ut.getAction())) {
            User u = userService.findById(ut.getUid());
            if (u != null && !u.isConfirmed()) {
                userService.setConfirmed(ut.getUid());
                releaseToken(token);
                msg = Message.Success(i18n.T("logs.success"), null, new Link("users.sign_in", i18n.T("form.user.sign_in.submit")));
            } else {
                msg = Message.Warning(i18n.T("logs.failed"), i18n.T("errors.user.bad_status"), new Link("users.sign_in", i18n.T("form.user.sign_in.submit")));
            }
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
    Response postUnlock(@RequestBody @Valid EmailFm fm, BindingResult result, Locale locale)  throws Exception{
        Response res = new Response(result);
        if (res.isOk()) {
            User u = userService.findByEmail(fm.getEmail());
            if (u == null) {
                res.addError(i18n.T("errors.user.not_found"));

            } else {
                if (u.isLocked()) {
                    sendTokenMail(fm.getEmail(), "/users/unlock", new Token(u.getId(), "users.unlock"), locale);
                    res.addData(i18n.T("form.user.unlock.success"));

                } else {
                    res.addError(i18n.T("errors.user.bad_status"));
                }

            }
        }
        return res;
    }

    @RequestMapping(value = "/unlock/{token}", method = RequestMethod.GET)
    RedirectView getUnlockToken(@PathVariable("token") String token)  throws Exception{
        Message msg;
        Token ut = string2token(token);
        if (ut != null && "users.unlock".equals(ut.getAction())) {
            User u = userService.findById(ut.getUid());
            if (u != null && u.isLocked()) {
                userService.setLocked(ut.getUid(), null);
                releaseToken(token);
                msg = Message.Success(i18n.T("logs.success"), null, new Link("users.sign_in", i18n.T("form.user.sign_in.submit")));
            } else {
                msg = Message.Warning(i18n.T("logs.failed"), i18n.T("errors.user.bad_status"), new Link("users.sign_in", i18n.T("form.user.sign_in.submit")));
            }
        } else {
            msg = Message.Error(i18n.T("logs.failed"), i18n.T("errors.user.bad_token"), null);
        }
        return doShow(msg);
    }


    @Autowired
    UserService userService;
}
