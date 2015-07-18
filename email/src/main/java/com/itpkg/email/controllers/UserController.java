package com.itpkg.email.controllers;

import com.itpkg.core.services.I18nService;
import com.itpkg.core.web.widgets.Form;
import com.itpkg.core.web.widgets.Response;
import com.itpkg.core.web.widgets.SelectField;
import com.itpkg.email.models.User;
import com.itpkg.email.services.EmailService;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.AssertTrue;

/**
 * Created by flamen on 15-7-18.
 */
@Controller("/email/users")
public class UserController {

    class UserFm {
        @NotEmpty
        @Range(min = 1, max = 64)
        String username;
        @NotEmpty
        long domain;
        @NotEmpty
        @Range(min = 6, max = 128)
        String password;
        String passwordConfirm;

        @AssertTrue
        boolean isValid() {
            return password.equals(passwordConfirm);
        }


    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseBody
    Form add() {
        Form fm = new Form("new", i18n.T("form.email.title.add_user"), "/email/users");
        fm.addTextField("username", i18n.T("form.email.user.name"));
        SelectField<Long> domains = emailService.getDomainMap();
        domains.setId("domain");
        domains.setName(i18n.T("form.email.user.domain"));
        fm.addField(domains);
        fm.addPasswordField("password", i18n.T("form.email.user.password"));
        fm.addPasswordField("passwordConfirm", i18n.T("form.email.user.password_confirm"));
        fm.addSubmit(i18n.T("form.buttons.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;


    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    Response create(@RequestBody UserFm fm, BindingResult result) {
        Response rs = new Response(result);
        if (rs.isOk()) {
            User u = emailService.createUser(fm.domain, fm.username, fm.password);
            if (u == null) {
                rs.addError(i18n.T("form.email.error.add_user"));
            } else {
                rs.addData(u);
            }
        }
        return rs;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    Response show(@PathVariable long id) {
        emailService.removeUser(id);
        return new Response(true);
    }

    @Autowired
    EmailService emailService;
    @Autowired
    I18nService i18n;
}
