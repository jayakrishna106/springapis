package com.mytest.webapi.controller;

import com.mytest.webapi.model.dto.UserInfoDto;
import com.mytest.webapi.service.SerializeService;
import com.mytest.webapi.service.UserService;
import com.mytest.webapi.util.CommandExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserInfoController {
    private final UserService userService;
    private final CommandExecutor commandExecutor;
    private final SerializeService serializeService;

    @GetMapping(value = "/user-info")
    public String getInfo(Model model){
        model.addAttribute("user_page", "user-page.html");
        model.addAttribute("userInfo", userService.getAllInfo());
        model.addAttribute("userInfoDto", new UserInfoDto());
        return "user-info";
    }

    @PostMapping(value = "/user-info")
    public ModelAndView addInfo(
            @ModelAttribute("userInfoDto") @Valid UserInfoDto userInfoDto,
            BindingResult result){
        if(!result.hasErrors()){
            userService.addInfo(userInfoDto);
            serializeService.serialize(userInfoDto);
            return new ModelAndView("redirect:/user-info");
        }
        else
            throw new IllegalArgumentException("adding user information failure");
    }

    @PostMapping(value = "user-info/update")
    public ModelAndView updateInfo(
            @ModelAttribute("userInfoDto") @Valid UserInfoDto userInfoDto,
            BindingResult result){
        if (!result.hasErrors()){
            userService.updateInfo(userInfoDto);
            serializeService.serialize(userInfoDto);
            return new ModelAndView("redirect:/user-Info");
        }
        else
            throw new IllegalArgumentException("updating user information failure");
    }

    @GetMapping(value = "/user-info/pingTest")
    public String pingTestInfo(Model model){
        model.addAttribute("pingInfo", commandExecutor.executeWithPing());
            return "ping_model";

    }
}
