package com.mytest.webapi.lisener.authentication;

import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public abstract class AuthenticationListener {

    public String getUserName(AbstractAuthenticationEvent event){
        return event.getAuthentication().getName();
    }
    public String getIpUser(AbstractAuthenticationEvent event){
        return ((WebAuthenticationDetails) event.getAuthentication()
        .getDetails()).getRemoteAddress();
    }
}
