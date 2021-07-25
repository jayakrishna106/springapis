package com.mytest.webapi.lisener.authentication;

import com.mytest.webapi.service.UserLoginDateService;
import com.mytest.webapi.service.impl.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessListener extends AuthenticationListener
    implements ApplicationListener<AuthenticationSuccessEvent> {

    private final LoginAttemptService loginAttemptService;
    private final UserLoginDateService userLoginDateService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event){
        userLoginDateService.saveUserLoginDateSuccess(
                getUserName(event),getIpUser(event));
        loginAttemptService.loginSucceeded(getUserName(event));
    }
}
