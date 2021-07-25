package com.mytest.webapi.service.impl;

import com.mytest.webapi.model.Role;
import com.mytest.webapi.model.User;
import com.mytest.webapi.security.encryption.userDetails.MyUserDetail;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsFactory {
    public UserDetailsFactory(){}

    public static MyUserDetail create(User user){
            return MyUserDetail.builder().id(user.getId())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .enable(true)
                    .authorities(mapToGrantedAuthority(user.getRoles()))
                    .build();
        }


    private static List<GrantedAuthority> mapToGrantedAuthority(List<Role> roleForUsers) {
        return roleForUsers.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
    }
}
