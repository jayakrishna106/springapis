package com.mytest.webapi.controller;

import com.mytest.webapi.constraint.IncludePath;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String root(Model model){
        model.addAttribute("greeting", IncludePath.REMOTE_PATH);
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }
}
