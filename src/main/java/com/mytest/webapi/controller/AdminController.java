package com.mytest.webapi.controller;

import com.mytest.webapi.constraint.Roles;
import com.mytest.webapi.model.dto.UpdateUserDto;
import com.mytest.webapi.service.AdminService;
import com.mytest.webapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminSerice;
    private final UserService userService;

    @PreAuthorize(Roles.AUTHORITY_ADMIN)
    @GetMapping("/admin-page")
    public String adminPage(Model model){
        model.addAttribute("users", userService.findAll());
        return "admin-page";
    }

    @PreAuthorize(Roles.AUTHORITY_ADMIN)
    @PostMapping("/admin-page/delete")
    public String deleteUser(@RequestParam(value = "id") Long id){
        adminSerice.removeUser(id);
        return "redirect:/admin-page";
    }

    @PreAuthorize(Roles.AUTHORITY_ADMIN)
    @PostMapping("/admin-page/update")
    public String updateUserLogin(@RequestParam(value = "old_userMail") String old_userMail,
                                  String userMail){
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setOldUserMail(old_userMail);
        updateUserDto.setNewUserMail(userMail);
        adminSerice.updateUsers(updateUserDto);
        return "redirect:/admin-page";
    }

}
