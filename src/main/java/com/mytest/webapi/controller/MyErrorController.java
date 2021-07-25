package com.mytest.webapi.controller;

import com.mytest.webapi.model.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class MyErrorController implements ErrorController {
    private static final String ERROR_PATH = "/error";

    @Value("${spring.debug.level}")
    private boolean debug;

    @Autowired
    private ErrorAttributes errorAttributes;

    @GetMapping(value = ERROR_PATH)
    String error(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ){
        model.addAttribute("error", new Error(response.getStatus(), getErrorAttribute(request, debug)));
        return  "error";
    }

    @Override
    public String getErrorPath(){
        return ERROR_PATH;
    }

    private Map<String, Object> getErrorAttribute(
            HttpServletRequest request,
            boolean isStackTrace
    ){
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        return this.errorAttributes.getErrorAttributes(servletWebRequest, isStackTrace);
    }
}
