package com.mytest.webapi.service.impl;

import com.mytest.webapi.model.User;
import com.mytest.webapi.model.UserLoginDate;
import com.mytest.webapi.model.factory.UserLoginDateFactory;
import com.mytest.webapi.repository.UserLoginDateRepository;
import com.mytest.webapi.service.UserLoginDateService;
import com.mytest.webapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserLoginDateServiceImpl implements UserLoginDateService {
    private final UserLoginDateRepository userLoginDateRepository;
    private final UserService userService;

    @Override
    public List<UserLoginDate> findAll() {
        return userLoginDateRepository.findAll();
    }

    @Override
    public UserLoginDate findById(long id) {
        return userLoginDateRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public UserLoginDate save(UserLoginDate userLoginDate) {
        return userLoginDateRepository.save(userLoginDate);
    }

    @Override
    public void delete(UserLoginDate userLoginDate) {
        userLoginDateRepository.delete(userLoginDate);
    }

    @Override
    public List<UserLoginDate> getFirstUserLoginDate(int firstCount, User user) {
        return userLoginDateRepository.findByUserOrderByLoginDateDesc(user, PageRequest.of(0,firstCount));
    }

    @Override
    public UserLoginDate saveUserLoginDateSuccess(String userName, String ip) {
        UserLoginDate userLoginDate = saveUserLoginDate(userName, ip, UserLoginDateFactory :: getUserLoginDateSuccess);
        log.warn("{} successful entry", userLoginDate.getId());
        return userLoginDate;
    }

    @Override
    public UserLoginDate saveUserLoginDateFailure(String userName, String ip) {
        UserLoginDate userLoginDate = saveUserLoginDate(userName, ip, UserLoginDateFactory :: getUserLoginDateFailure);
        log.warn("{} failure entry", userLoginDate.getId());
        return userLoginDate;
    }

    private UserLoginDate saveUserLoginDate(String userName, String ip, SaveUserLoginDate factoryRef){
        User user = userService.findByUserNameOrEmail(userName.toLowerCase());
        UserLoginDate userLoginDate = factoryRef.getUser(user, ip);
        UserLoginDate loginDateFailure = save(userLoginDate);
        return loginDateFailure;
    }

    private interface SaveUserLoginDate{
        UserLoginDate getUser(User user, String ip);
    }
}
