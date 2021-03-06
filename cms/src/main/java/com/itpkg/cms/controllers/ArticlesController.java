package com.itpkg.cms.controllers;

import com.itpkg.core.web.widgets.Form;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by flamen on 15-7-20.
 */
@Controller("cms.controllers.articles")
@RequestMapping("/cms/articles")
public class ArticlesController {

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    @ResponseBody
    public Form edit( @PathVariable long id) {
        Form fm = new Form("newArticle", "", "");
        return fm;
    }
}
