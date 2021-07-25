package com.mytest.webapi.mapper;

import com.mytest.webapi.model.User;
import com.mytest.webapi.model.dto.UserDto;
import com.mytest.webapi.model.dto.UserPasswordDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    UserDto toUserDto(User user);
    User toUser(UserDto userDto);
    User toUser(UserPasswordDto userPasswordDto);
    List<User> toListUsers(List<UserDto> userDtos);
    List<UserDto> toListUserDto(List<User> users);
}
