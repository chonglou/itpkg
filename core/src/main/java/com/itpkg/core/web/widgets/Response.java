package com.itpkg.core.web.widgets;

import org.springframework.validation.BindingResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by flamen on 15-7-17.
 */
public class Response implements Serializable {
    public Response() {
        errors = new ArrayList<>();
        data = new ArrayList<>();
        created = new Date();
    }

    public Response(boolean ok){
        this.ok = ok;
    }

    public Response(BindingResult result) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach((e) -> errors.add(e.toString()));
        } else {
            ok = true;
        }
    }

    public void addError(Object error) {
        ok = false;
        errors.add(error);
    }

    public void addData(Object obj) {
        data.add(obj);
    }

    private boolean ok;
    private List<Object> errors;
    private List<Object> data;
    private Date created;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
