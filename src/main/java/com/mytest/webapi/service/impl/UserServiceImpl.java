package com.mytest.webapi.service.impl;

import com.mytest.webapi.LdapClient.LdapClient;
import com.mytest.webapi.constraint.Role;
import com.mytest.webapi.exception.UserNotFoundException;
import com.mytest.webapi.mapper.UserInfoMapper;
import com.mytest.webapi.mapper.UserMapper;
import com.mytest.webapi.model.User;
import com.mytest.webapi.model.UserInfo;
import com.mytest.webapi.model.dto.UserDto;
import com.mytest.webapi.model.dto.UserInfoDto;
import com.mytest.webapi.model.dto.UserPasswordDto;
import com.mytest.webapi.model.dto.UserRegistrationDto;
import com.mytest.webapi.repository.RolesRepository;
import com.mytest.webapi.repository.UserInfoRepository;
import com.mytest.webapi.repository.UserRepository;
import com.mytest.webapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;
    private final UserInfoMapper userInfoMapper;
    private final UserMapper userMapper;
    private final LdapClient ldapClient;

    @Override
    public User findByUserName(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User findByUserNameOrEmail(String name) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrderByEmail(name).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    @Override
    public User save(UserRegistrationDto registration) throws RoleNotFoundException {
        String email = registration.getEmail().toLowerCase();
        String username = email.substring(0, email.indexOf("@"));
        List roles = getRoleOfStandardUser();

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRoles(roles);
        User userSaved = userRepository.save(user);
        log.info("User registered with id: {}", userSaved.getId());
        return userSaved;
    }

    @Transactional
    @Override
    public void updateNewPasswordForUser(UserPasswordDto userPasswordDto) {
        User userAuth = getContextUser();
        if (passwordEncoder.matches(userPasswordDto.getOldPassword(), userAuth.getPassword())){
            if (userPasswordDto.getNewPassword().equals(userPasswordDto.getConfirmPassword())){
                userAuth.setPassword(passwordEncoder.encode(userPasswordDto.getNewPassword()));
                userRepository.save(userAuth);
                ldapClient.modify(userAuth.getEmail().toLowerCase(),userPasswordDto.getNewPassword());
                log.info("{} changed password", userAuth.getId());
                return;
            }
            log.info("{} changes the password and entered it incorrectly", userAuth.getId());
        }
        log.info("Password for user : {} is different", userAuth.getId());
    }

    private List<com.mytest.webapi.model.Role> getRoleOfStandardUser() throws RoleNotFoundException{
        return Collections.singletonList(rolesRepository.findByRoleName(Role.ROLE_USER.toString()).orElseThrow(
                () -> new RoleNotFoundException("Role not found")));
    }

    @Override
    public User getContextUser() {
        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()
        )                   .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public void updatePassword(String password, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){
            user.get().setPassword(passwordEncoder.encode(password));
            userRepository.save(user.get());
            log.info("Successfully updated user password for user: {}", user.get().getId());
        }else throw new UserNotFoundException("User not found. Failed to update user password.");
    }

    @Override
    public void addInfo(UserInfoDto userInfoDto) {
        User user = getContextUser();
        UserInfo userInfo = userInfoMapper.toUserInfo(userInfoDto);
        userInfo.setUserId(user.getId());
        userInfoRepository.save(userInfo);
        log.info("Successfully added user information for user : {}", user.getId());
    }

    @Override
    public UserInfoDto getAllInfo() {
        User user = getContextUser();
        return userInfoMapper.toUserInfoDto(userInfoRepository.findByUserId(user.getId()));
    }

    @Override
    public void updateInfo(UserInfoDto userInfoDto) {
        User user = getContextUser();
        UserInfo userInfo = user.getUserInfo();
        UserInfo updateUserInfo = userInfoMapper.toUserInfo(userInfoDto);
        updateUserInfo.setId(userInfo.getId());
        updateUserInfo.setUserId(userInfo.getUserId());
        updateUserInfo.setCreatedAt(userInfo.getCreatedAt());
        userInfoRepository.save(updateUserInfo);
        log.info("User information updated successfully for user : {}", user.getId());
    }

    @Override
    public List<UserDto> findAll() {
        List<UserDto> users = userMapper.toListUserDto(userRepository.findAll());
        log.info("get all users for admin");
        return users;
    }
}
