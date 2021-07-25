package com.mytest.webapi.mapper;

import com.mytest.webapi.model.User;
import com.mytest.webapi.model.User.UserBuilder;
import com.mytest.webapi.model.dto.UserDto;
import com.mytest.webapi.model.dto.UserPasswordDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.11 (Ubuntu)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setUsername( user.getUsername() );
        userDto.setEmail( user.getEmail() );

        return userDto;
    }

    @Override
    public User toUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.id( userDto.getId() );
        user.username( userDto.getUsername() );
        user.email( userDto.getEmail() );

        return user.build();
    }

    @Override
    public User toUser(UserPasswordDto userPasswordDto) {
        if ( userPasswordDto == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        return user.build();
    }

    @Override
    public List<User> toListUsers(List<UserDto> userDtos) {
        if ( userDtos == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( userDtos.size() );
        for ( UserDto userDto : userDtos ) {
            list.add( toUser( userDto ) );
        }

        return list;
    }

    @Override
    public List<UserDto> toListUserDto(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( users.size() );
        for ( User user : users ) {
            list.add( toUserDto( user ) );
        }

        return list;
    }
}
