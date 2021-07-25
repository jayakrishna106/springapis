package com.mytest.webapi.controller;

import com.mytest.webapi.model.dto.UserPasswordDto;
import com.mytest.webapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserChangePasswordController {
    private final UserService userService;

    @PostMapping("/change-password/update")
    public String changePassword(@ModelAttribute("userPass") @Valid UserPasswordDto userPasswordDto,
                                 BindingResult result){
        if(result.hasErrors()){
            return "change-password";
        }
        userService.updateNewPasswordForUser(userPasswordDto);
        return "redirect:/user-page";
    }

    @GetMapping("/change-password")
    public String showUserPasswordForm(Model model){
        model.addAttribute("userPass", new UserPasswordDto());
        return "change-password";
    }
}
