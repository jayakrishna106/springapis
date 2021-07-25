package com.mytest.webapi.controller;

import com.mytest.webapi.model.dto.PasswordResetDto;
import com.mytest.webapi.model.dto.PasswordResetTokenDto;
import com.mytest.webapi.service.ResetPasswordService;
import com.mytest.webapi.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class PasswordResetController {
    private final TokenService tokenService;
    private final ResetPasswordService resetPasswordService;

    @ModelAttribute("passwordResetForm")
    public PasswordResetDto passwordReset(){
        return new PasswordResetDto();
    }

    @GetMapping(value = "/reset-password")
    public String displayResetPasswordPage(
            @RequestParam(required = false) String token,
            Model model){
        PasswordResetTokenDto  resetToken = tokenService.findByToken(token);
        if(resetToken==null){
            model.addAttribute("error", "Could not find password reset token");
        }
        else if(resetToken.isExpired()){
            model.addAttribute("error", "Token has expired, please request a new password reset.");
        }
        else{
            model.addAttribute("token", resetToken.getToken());
        }
        return "reset-password";
    }

    @PostMapping(value = "/reset-password")
    @Transactional
    public String handlePasswordReset(@ModelAttribute("passwordResetForm")
                                      @Valid PasswordResetDto form,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            redirectAttributes.addAttribute(BindingResult.class.getName() + ".passwordResetForm", result);
            redirectAttributes.addFlashAttribute("passwordResetForm", form);
            return "redirect:/reset-password?token=" + form.getToken();
        }

        resetPasswordService.resetPassword(form);
        return "redirect:/login?resetSuccess";
    }
}
