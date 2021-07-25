package com.mytest.webapi.model.factory;

import com.mytest.webapi.model.User;
import com.mytest.webapi.model.UserLoginDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class UserLoginDateFactory {

    public static UserLoginDate getUserLoginDateFailure(User user, String ipUser){
        return getUserLoginDate(user, ipUser, false);
    }

    public static UserLoginDate getUserLoginDateSuccess(User user, String ipUser){
        return  getUserLoginDate(user, ipUser, true);
    }

    private static UserLoginDate getUserLoginDate(User user, String ipUser, boolean loginStatus){
        UserLoginDate userLoginDate = new UserLoginDate();
        userLoginDate.setLoginDate(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
        userLoginDate.setSuccessfulLogin(loginStatus);
        userLoginDate.setUser(user);
        userLoginDate.setIpUser(ipUser);
        userLoginDate.setId(null);
        return userLoginDate;
    }
}
