package com.mytest.webapi.service;

import com.mytest.webapi.model.User;
import com.mytest.webapi.model.UserLoginDate;

import java.util.List;

public interface UserLoginDateService {

    List<UserLoginDate> findAll();
    UserLoginDate findById(long id);
    UserLoginDate save(UserLoginDate userLoginDate);
    void delete(UserLoginDate userLoginDate);
    List<UserLoginDate> getFirstUserLoginDate(int firstCount, User user);
    UserLoginDate saveUserLoginDateSuccess(String userName, String ip);
    UserLoginDate saveUserLoginDateFailure(String userName, String ip);
}
