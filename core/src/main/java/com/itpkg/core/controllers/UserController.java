package com.itpkg.core.controllers;

import com.itpkg.core.models.User;
import com.itpkg.core.services.I18nService;
import com.itpkg.core.services.UserService;
import com.itpkg.core.utils.EmailHelper;
import com.itpkg.core.utils.EncryptHelper;
import com.itpkg.core.web.widgets.Form;
import com.itpkg.core.web.widgets.Response;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.AssertTrue;
import java.io.IOException;

/**
 * Created by flamen on 15-7-14.
 */


@Controller
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public class UserToken {
        public long id;
    }

    class SignInFm {
        @NotEmpty
        @Email
        String email;
        @NotEmpty
        String password;
    }


    class SignUpFm {
        @NotEmpty
        @Range(min = 2, max = 32)
        String username;
        @NotEmpty
        @Email
        String email;
        @NotEmpty
        @Range(min = 6, max = 128)
        String password;
        String passwordConfirm;

        @AssertTrue
        boolean isValid() {
            return password.equals(passwordConfirm);
        }
    }

    class EmailFm {
        @NotEmpty
        @Email
        String email;
    }

    @RequestMapping(value = "/user/sign_in", method = RequestMethod.GET)
    @ResponseBody
    Form getSignIn() {
        Form fm = new Form("sign_in", i18n.T("form.user.sign_in.title"), "/user/sign_in");
        fm.addEmailField("email", i18n.T("form.fields.email"));
        fm.addPasswordField("password", i18n.T("form.fields.password"));
        fm.addSubmit(i18n.T("form.user.sign_in.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/user/sign_in", method = RequestMethod.POST)
    @ResponseBody
    Response postSignIn(@RequestBody SignInFm fm, BindingResult result) {

        Response res = new Response(result);
        if (res.isOk()) {
            User u = userService.auth(fm.email, fm.password);
            if (u == null) {
                res.addError(i18n.T("form.user.sign_in.failed"));
            } else {
                UserToken ut = new UserToken();
                ut.id = u.getId();
                try {
                    res.addData(encryptHelper.payload2token("Sign in", ut, 60 * 24));
                } catch (IOException | JoseException e) {
                    res.addError(e.getMessage());
                }
            }
        }
        return res;
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
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/user/sign_up", method = RequestMethod.POST)
    @ResponseBody
    Response postSignUp(@RequestBody SignUpFm fm, BindingResult result) {
        Response res = new Response(result);
        if (res.isOk()) {

            User u = userService.create(fm.username, fm.email, fm.password);
            if (u == null) {
                res.addError(i18n.T("form.user.sign_up.failed"));
            } else {
                try {
                    sendMail(u.getId(), fm.email, "confirm");
                } catch (Exception e) {
                    logger.error("Sign up error", e);
                    res.addError(e.getMessage());
                }
            }
        }
        return res;
    }

    private void sendMail(long uid, String email, String action) throws Exception {

        UserToken ut = new UserToken();
        ut.id = uid;
        String token = encryptHelper.payload2token(action, ut, 30);
        String subject = i18n.T("mail.user." + action.toLowerCase() + ".subject");
        String body = i18n.T("mail.user." + action.toLowerCase() + ".body", email, token);
        emailHelper.send(email, subject, body);

    }

    @RequestMapping(value = "/user/forgot_password", method = RequestMethod.GET)
    @ResponseBody
    Form getForgotPassword() {
        Form fm = new Form("forgot_password", i18n.T("form.user.forgot_password.title"), "/user/forgot_password");
        fm.addEmailField("email", i18n.T("form.fields.email"));
        fm.addSubmit(i18n.T("form.user.forgot_password.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/user/forgot_password", method = RequestMethod.POST)
    @ResponseBody
    Response postForgotPassword(@RequestBody SignUpFm fm, BindingResult result) {
        Response res = new Response(result);
        if(res.isOk()){
            User u = userService.findByEmail(fm.email);
            if (u == null) {
                res.addError(i18n.T("form.user.email_not_exists"));
            } else {
                try {
                    //todo
                    sendMail(u.getId(), fm.email, "Change password");
                } catch (Exception e) {
                    logger.error("Forgot password error", e);
                    res.addError(e.getMessage());
                }
            }
        }
        return res;
    }

    @RequestMapping(value = "/user/change_password", method = RequestMethod.GET)
    @ResponseBody
    Form getChangePassword() {
        Form fm = new Form("change_password", i18n.T("form.user.change_password.title"), "/user/change_password");
        fm.addEmailField("email", i18n.T("form.fields.email"));
        fm.addSubmit(i18n.T("form.user.change_password.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/user/change_password", method = RequestMethod.POST)
    @ResponseBody
    Response postChangePassword() {
        Response res = new Response();
        return res;
    }

    @RequestMapping(value = "/user/confirm", method = RequestMethod.GET)
    @ResponseBody
    Form getConfirm() {
        Form fm = new Form("confirm", i18n.T("form.user.confirm.title"), "/user/confirm");
        fm.addEmailField("email", i18n.T("form.fields.email"));
        fm.addSubmit(i18n.T("form.user.confirm.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/user/confirm", method = RequestMethod.POST)
    @ResponseBody
    Response postConfirm() {
        Response res = new Response();
        return res;
    }


    @RequestMapping(value = "/user/unlock", method = RequestMethod.GET)
    @ResponseBody
    Form getUnlock() {
        Form fm = new Form("unlock", i18n.T("form.user.unlock.title"), "/user/unlock");
        fm.addEmailField("email", i18n.T("form.fields.email"));
        fm.addSubmit(i18n.T("form.user.unlock.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/user/unlock", method = RequestMethod.POST)
    @ResponseBody
    Response postUnlock() {
        Response res = new Response();
        return res;
    }

    @RequestMapping(value = "/user/token", method = RequestMethod.GET)
    @ResponseBody
    Response getToken() {
        Response res = new Response();
        return res;
    }


    @Autowired
    UserService userService;

    @Autowired
    I18nService i18n;
    @Autowired
    EncryptHelper encryptHelper;
    @Autowired
    EmailHelper emailHelper;
}
