package com.mytest.webapi.service;

import com.mytest.webapi.LdapClient.LdapClient;
import com.mytest.webapi.model.User;
import com.mytest.webapi.model.dto.UpdateUserDto;
import com.mytest.webapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdminService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final LdapClient ldapClient;

    public  void removeUser(Long id){
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent() &&userService.getContextUser().equals(
                user.get().getUsername())){
            return;
        }

        if (user.isPresent()){
            user.get().getRoles().clear();
            user.get().setEnable(true);
            userRepository.save(user.get());
            ldapClient.deleteUser(id);
            userRepository.deleteById(id);

            log.info("Admin removed user by Id : " + id);
        }
    }

    public void updateUsers(UpdateUserDto updateUserDto){
        Optional<User> user = userRepository.findByUsername(updateUserDto.getOldUserMail());

        if(user.isPresent()){
            user.get().setEmail(updateUserDto.getNewUserMail());
            userRepository.save(user.get());
            ldapClient.modify(updateUserDto.getNewUserMail().toLowerCase(), user.get().getPassword());
        }else throw new UsernameNotFoundException("user not found");

        log.info("User login successfully changed to " + updateUserDto.getNewUserMail());

    }
}
