package com.mytest.webapi.controller;

import com.mytest.webapi.model.dto.PasswordForgotDto;
import com.mytest.webapi.service.RecaptchaService;
import com.mytest.webapi.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Log4j2
@Controller
@RequiredArgsConstructor
public class PasswordForgotController {

    private final ResetPasswordService resetPasswordService;
    private final RecaptchaService recaptchaService;

    @ModelAttribute("forgotPasswordForm")
    public PasswordForgotDto forgotDto(){
        return new PasswordForgotDto();
    }

    @GetMapping(value = "/forgot-password")
    public String displayForgotPasswordPage(Model model){
        model.addAttribute("sitekey", recaptchaService.getRecaptchaPublic());
        return "forgot-password";
    }

    @PostMapping(value = "/forgot-password")
    public String processForgotPasswordForm(
            @ModelAttribute("forgotPasswordForm") @Valid PasswordForgotDto form,
            BindingResult result,
            HttpServletRequest request
    ){
        resetPasswordService.sendMail(request, form, result);
        return "forgot-password";
    }
}
