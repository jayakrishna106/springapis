package com.mytest.webapi.security.encryption.userDetails;

import com.mytest.webapi.exception.BruteForcePasswordException;
import com.mytest.webapi.model.User;
import com.mytest.webapi.model.UserLoginDate;
import com.mytest.webapi.service.UserLoginDateService;
import com.mytest.webapi.service.UserService;
import com.mytest.webapi.service.impl.LoginAttemptService;
import com.mytest.webapi.service.impl.UserDetailsFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final UserService userService;
    private final UserLoginDateService userLoginDateService;
    private final HttpServletRequest request;
    private final LoginAttemptService loginAttemptService;

    @Value("${protection.timeout.numberofattempts}")
    private int numberOfAttempts;

    @Value("${protection.timeout.minutes}")
    private int lookTimeUserMinutes;

    @Override
    public UserDetails loadUserByUsername(String email){
        checkIPofUserIsBlocked();
        User user = userService.findByEmail(email);
        checkBruteForce(user);
        return UserDetailsFactory.create(user);
    }

    private void checkIPofUserIsBlocked(){
        if(loginAttemptService.isBlocked(request.getRemoteAddr())){
            throw new BruteForcePasswordException();
        }
    }

    private void checkBruteForce(User user){
        List<UserLoginDate> userLoginDates = userLoginDateService.getFirstUserLoginDate(numberOfAttempts, user);
        long serverTime = LocalDateTime.now().atZone(
                ZoneId.systemDefault()).toInstant().toEpochMilli();
        long blockingTimeInMillis = TimeUnit.MINUTES.toMillis(lookTimeUserMinutes);
        long unsuccessfulUserEntriesPerTime =
                userLoginDates.stream().filter(item -> !item.isSuccessfulLogin()
                                    && serverTime - item.getLoginDate().getTime() < blockingTimeInMillis).count();

        if(unsuccessfulUserEntriesPerTime >= numberOfAttempts){
            log.warn("Password brute force occurred. User Id :" + user.getId());
            throw new UsernameNotFoundException("Invalid username and password.");
        }
    }
}
