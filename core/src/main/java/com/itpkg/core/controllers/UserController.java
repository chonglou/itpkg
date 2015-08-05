package com.itpkg.core.controllers;

import com.itpkg.core.auth.Credential;
import com.itpkg.core.forms.EmailFm;
import com.itpkg.core.forms.PasswordFm;
import com.itpkg.core.forms.SignInFm;
import com.itpkg.core.forms.SignUpFm;
import com.itpkg.core.models.User;
import com.itpkg.core.services.UserService;
import com.itpkg.core.utils.EngineHelper;
import com.itpkg.core.web.widgets.Form;
import com.itpkg.core.web.widgets.Link;
import com.itpkg.core.web.widgets.Message;
import com.itpkg.core.web.widgets.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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


    @RequestMapping(value = "/nav", method = RequestMethod.GET)
    @ResponseBody
    public Response getNav(Authentication auth) {
        Response rs = new Response(true);

        rs.addData(i18n.T("site.title"));

        rs.addData(new Link("home", i18n.T("pages.home.title")));
        for (String en : engineHelper.getEngines()) {
            if (engineHelper.isEnable(en)) {
                rs.addData(new Link("engine." + en, i18n.T("engine." + en + ".name")));
            }
        }
        rs.addData(new Link("about_us", i18n.T("pages.about_us.title")));
        return rs;
    }

    @RequestMapping(value = "/bar", method = RequestMethod.GET)
    @ResponseBody
    public Response getPersonalBar(Authentication auth) {

        Response rs = new Response(true);
        if (auth != null && auth.isAuthenticated()) {
            rs.addData(i18n.T("user.personal_center"));
            rs.addData(new Link("users.profile", i18n.T("form.user.profile.title")));
        } else {
            rs.addData(i18n.T("user.sign_in_or_up"));
            for (String s : new String[]{"sign_in", "sign_up", "forgot_password", "confirm", "unlock"}) {
                rs.addData(new Link("users." + s, i18n.T("form.user." + s + ".title")));
            }
        }
        return rs;
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/sign_in", method = RequestMethod.GET)
    @ResponseBody
    public Form getSignIn() {
        Form fm = new Form("sign_in", i18n.T("form.user.sign_in.title"), "/users/sign_in");
        fm.addEmailField("email", i18n.T("form.fields.email"), true, i18n.T("form.placeholders.email"));
        fm.addPasswordField("password", i18n.T("form.fields.password"), true, i18n.T("form.placeholders.password"));
        fm.addSubmit(i18n.T("form.user.sign_in.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/sign_in", method = RequestMethod.POST)
    @ResponseBody
    public Response postSignIn(@RequestBody @Valid SignInFm fm, BindingResult result) throws Exception {

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
                    res.addData(jwtHelper.payload2token("/users/sign_in", new Credential(u, "users.sign_in"), 60 * 24 * 7));
                }
            }
        }
        return res;
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/sign_up", method = RequestMethod.GET)
    @ResponseBody
    public Form getSignUp() {
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

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/sign_up", method = RequestMethod.POST)
    @ResponseBody
    public Response postSignUp(@RequestBody @Valid SignUpFm fm, BindingResult result, Locale locale) throws Exception {
        Response res = new Response(result);
        if (res.isOk()) {
            User u = userService.create(fm.getUsername(), fm.getEmail(), fm.getPassword());
            if (u == null) {
                res.addError(i18n.T("form.user.sign_up.failed"));
            } else {
                sendTokenMail(fm.getEmail(), "/users/confirm", new Credential(u, "users.confirm"), locale);
                res.addData(i18n.T("form.user.confirm.success"));
            }
        }
        return res;
    }


    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/forgot_password", method = RequestMethod.GET)
    @ResponseBody
    public Form getForgotPassword() {
        Form fm = new Form("forgot_password", i18n.T("form.user.forgot_password.title"), "/users/forgot_password");
        fm.addEmailField("email", i18n.T("form.fields.email"), true);
        fm.addSubmit(i18n.T("form.user.forgot_password.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/forgot_password", method = RequestMethod.POST)
    @ResponseBody
    public Response postForgotPassword(@RequestBody @Valid EmailFm fm, BindingResult result, Locale locale) throws Exception {
        Response res = new Response(result);
        if (res.isOk()) {
            User u = userService.findByEmail(fm.getEmail());
            if (u == null) {
                res.addError(i18n.T("form.user.email_not_exists"));
            } else {
                Credential token = new Credential(u, "users.change_password");
                String code = token2string("/users/change_password", token, 30);
                String subject = i18n.T("mail." + token.getAction() + ".subject");
                String body = i18n.T(
                        "mail." + token.getAction() + ".body",
                        u.getEmail(),
                        String.format(
                                "%s/#/users/change_password?code=%s&locale=%s",
                                settingService.get("site.url", String.class), code, locale)
                );
                emailHelper.sendHtml(u.getEmail(), subject, body);
                res.addData(i18n.T("form.user.forgot_password.success"));
            }
        }
        return res;
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/change_password/{token}", method = RequestMethod.GET)
    @ResponseBody
    public Form getChangePassword(@PathVariable("token") String token) {
        Form fm = new Form("change_password", i18n.T("form.user.change_password.title"), "/users/change_password");
        fm.addHidden("token", token);
        fm.addPasswordField("password", i18n.T("form.fields.password"), true, i18n.T("form.placeholders.password"));
        fm.addPasswordField("passwordConfirm", i18n.T("form.fields.password_confirm"), true, i18n.T("form.placeholders.password_confirm"));
        fm.addSubmit(i18n.T("form.user.change_password.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);

        return fm;
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/change_password", method = RequestMethod.POST)
    @ResponseBody
    public Response postChangePassword(@RequestBody @Valid PasswordFm fm, BindingResult result) throws Exception {

        Response res = new Response(result);
        if (res.isOk()) {
            Credential ut = string2token(fm.getToken());
            if (ut != null && "users.change_password".equals(ut.getAction())) {
                User u = userService.findByToken(ut.getProvider(), ut.getToken());
                userService.setPassword(u.getId(), fm.getPassword());
                releaseToken(fm.getToken());
                res.addData(i18n.T("form.user.change_password.success"));
            } else {
                res.addError(i18n.T("errors.user.bad_token"));
            }
        }
        return res;
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    @ResponseBody
    public Form getConfirm() {
        Form fm = new Form("confirm", i18n.T("form.user.confirm.title"), "/users/confirm");
        fm.addEmailField("email", i18n.T("form.fields.email"), true);
        fm.addSubmit(i18n.T("form.user.confirm.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    @ResponseBody
    public Response postConfirm(@RequestBody @Valid EmailFm fm, BindingResult result, Locale locale) throws Exception {
        Response res = new Response(result);
        if (res.isOk()) {
            User u = userService.findByEmail(fm.getEmail());
            if (u == null) {
                res.addError(i18n.T("errors.user.not_found"));
            } else {
                if (u.isConfirmed()) {
                    res.addError(i18n.T("errors.user.bad_status"));
                } else {
                    sendTokenMail(fm.getEmail(), "/users/confirm", new Credential(u, "users.confirm"), locale);
                    res.addData(i18n.T("form.user.confirm.success"));
                }
            }
        }
        return res;
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/confirm/{token}", method = RequestMethod.GET)
    public RedirectView getConfirmToken(@PathVariable("token") String token) throws Exception {

        Message msg;
        Credential ut = string2token(token);
        if (ut != null && "users.confirm".equals(ut.getAction())) {
            User u = userService.findByToken(ut.getProvider(), ut.getToken());
            if (u != null && !u.isConfirmed()) {
                userService.setConfirmed(u.getId());
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


    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/unlock", method = RequestMethod.GET)
    @ResponseBody
    public Form getUnlock() {
        Form fm = new Form("unlock", i18n.T("form.user.unlock.title"), "/users/unlock");
        fm.addEmailField("email", i18n.T("form.fields.email"), true);
        fm.addSubmit(i18n.T("form.user.unlock.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/unlock", method = RequestMethod.POST)
    @ResponseBody
    public Response postUnlock(@RequestBody @Valid EmailFm fm, BindingResult result, Locale locale) throws Exception {
        Response res = new Response(result);
        if (res.isOk()) {
            User u = userService.findByEmail(fm.getEmail());
            if (u == null) {
                res.addError(i18n.T("errors.user.not_found"));

            } else {
                if (u.isLocked()) {
                    sendTokenMail(fm.getEmail(), "/users/unlock", new Credential(u, "users.unlock"), locale);
                    res.addData(i18n.T("form.user.unlock.success"));

                } else {
                    res.addError(i18n.T("errors.user.bad_status"));
                }

            }
        }
        return res;
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/unlock/{token}", method = RequestMethod.GET)
    public RedirectView getUnlockToken(@PathVariable("token") String token) throws Exception {
        Message msg;
        Credential ut = string2token(token);
        if (ut != null && "users.unlock".equals(ut.getAction())) {
            User u = userService.findByToken(ut.getProvider(), ut.getToken());
            if (u != null && u.isLocked()) {
                userService.setLocked(u.getId(), null);
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
    @Autowired
    EngineHelper engineHelper;
}
