package com.mytest.webapi.controller;

import com.mytest.webapi.model.dto.UserRegistrationDto;
import com.mytest.webapi.service.RecaptchaService;
import com.mytest.webapi.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.management.relation.RoleNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Log4j2
@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registraionService;
    private final RecaptchaService recaptchaService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto(){
        return new UserRegistrationDto();
    }
    @GetMapping(value = "/registration")
    public String showRegistrationForm(Model model){
        model.addAttribute("sitekey", recaptchaService.getRecaptchaPublic());
        return "registration";
    }

    @PostMapping(value = "/registration")
    public String registerUserAccount(
            @ModelAttribute("user") @Valid UserRegistrationDto userRegistrationDto,
            BindingResult result,
            HttpServletRequest request) throws RoleNotFoundException{
        if(!registraionService.register(request,userRegistrationDto, result)){
            return "registration";
        }
        return "redirect:/registration?success";
    }
}
