package com.mytest.webapi.lisener.authentication;

import com.mytest.webapi.service.UserLoginDateService;
import com.mytest.webapi.service.impl.LoginAttemptService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener extends AuthenticationListener
    implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAttemptService loginAttemptService;
    private final UserLoginDateService userLoginDateService;

    @Override
    public void onApplicationEvent(
            AuthenticationFailureBadCredentialsEvent event){
        userLoginDateService.saveUserLoginDateFailure(
                getUserName(event),
                getIpUser(event));
        loginAttemptService.loginFailed(getIpUser(event));
    }
}
