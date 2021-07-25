package com.mytest.webapi.service;

import com.mytest.webapi.model.User;
import com.mytest.webapi.model.dto.UserDto;
import com.mytest.webapi.model.dto.UserInfoDto;
import com.mytest.webapi.model.dto.UserPasswordDto;
import com.mytest.webapi.model.dto.UserRegistrationDto;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

public interface UserService {
    User findByUserName(String username);
    User findByUserNameOrEmail(String name);
    User findByEmail(String email);
    User save(UserRegistrationDto registration) throws RoleNotFoundException;
    void updateNewPasswordForUser(UserPasswordDto userPasswordDto);
    User getContextUser();
    void updatePassword(String password, Long userId);
    UserInfoDto getAllInfo();
    void addInfo(UserInfoDto userInfoDto);
    void updateInfo(UserInfoDto userInfoDto);
    List<UserDto> findAll();
}
