package com.itpkg.core.errors;

import com.itpkg.core.web.widgets.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by flamen on 15-7-15.
 */

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    Response userNotFoundExceptionHandler(UserNotFoundException ex) {
        Response r = new Response();
        r.addError(ex.getMessage());
        return r;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Response accessDeniedExceptionHandler(AccessDeniedException e) {
        Response rs = new Response();
        rs.addError(e.getMessage());
        return rs;
    }


}
