package com.itpkg.email.controllers;

import com.itpkg.core.services.I18nService;
import com.itpkg.core.web.widgets.Form;
import com.itpkg.core.web.widgets.Response;
import com.itpkg.email.forms.AliasFm;
import com.itpkg.email.models.Alias;
import com.itpkg.email.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by flamen on 15-7-18.
 */
@Controller("email.controllers.aliases")
@RequestMapping("/email/aliases")
@PreAuthorize("hasRole('ADMIN')")
public class AliasController {


    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseBody
    Form add() {
        Form fm = new Form("new", i18n.T("form.email.title.add_alias"), "/email/aliases");
        fm.addTextField("source", i18n.T("form.email.alias.source"), true);


        fm.addField(emailService.getDomainMap("domain", i18n.T("form.email.alias.domain")));

        fm.addField(emailService.getUserMap("destination", i18n.T("form.email.alias.destination")));

        fm.addSubmit(i18n.T("form.buttons.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;


    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    Response create(@RequestBody @Valid AliasFm fm, BindingResult result) {
        Response rs = new Response(result);
        if (rs.isOk()) {
            Alias a = emailService.createAlias(fm.getDomain(), fm.getSource(), fm.getDestination());
            if (a == null) {
                rs.addError(i18n.T("form.email.error.add_alias"));
            } else {
                rs.addData(a);
            }
        }
        return rs;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    Response destroy(@PathVariable long id) {
        emailService.removeAlias(id);
        return new Response(true);
    }

    @Autowired
    EmailService emailService;
    @Autowired
    I18nService i18n;
}
