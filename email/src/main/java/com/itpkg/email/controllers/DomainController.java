package com.itpkg.email.controllers;

import com.itpkg.core.services.I18nService;
import com.itpkg.core.web.widgets.Form;
import com.itpkg.core.web.widgets.Response;
import com.itpkg.email.forms.DomainFm;
import com.itpkg.email.models.Domain;
import com.itpkg.email.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by flamen on 15-7-18.
 */
@Controller("email.controllers.domains")
@RequestMapping("/email/domains")
@PreAuthorize("hasRole('ADMIN')")
public class DomainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    List<Domain> list() {
        return emailService.listAllDomain();
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseBody
    Form add() {
        Form fm = new Form("new", i18n.T("form.email.title.add_domain"), "/email/domains");
        fm.addTextField("name", i18n.T("form.email.domain.name"), true);
        fm.addSubmit(i18n.T("form.buttons.submit"));
        fm.addReset(i18n.T("form.buttons.reset"));
        fm.setOk(true);
        return fm;


    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    Domain show(@PathVariable long id) {
        return emailService.getDomain(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    Response destroy(@PathVariable long id) {
        emailService.removeDomain(id);
        return new Response();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    Response create(@RequestBody @Valid DomainFm fm, BindingResult result) {
        Response rs = new Response(result);
        if (rs.isOk()) {
            Domain d = emailService.createDomain(fm.getName());
            if (d == null) {
                rs.addError(i18n.T("form.email.error.add_domain"));
            } else {
                rs.addData(d);
            }
        }
        return rs;
    }


    @Autowired
    EmailService emailService;
    @Autowired
    I18nService i18n;
}
