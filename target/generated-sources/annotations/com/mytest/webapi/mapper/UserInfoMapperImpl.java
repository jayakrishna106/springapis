package com.mytest.webapi.mapper;

import com.mytest.webapi.model.UserInfo;
import com.mytest.webapi.model.dto.UserInfoDto;
import com.mytest.webapi.security.encryption.AES.CryptoSymmentricAES;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.11 (Ubuntu)"
)
@Component
public class UserInfoMapperImpl implements UserInfoMapper {

    @Autowired
    private CryptoSymmentricAES cryptoSymmentricAES;

    @Override
    public UserInfo toUserInfo(UserInfoDto userInfoDto) {
        if ( userInfoDto == null ) {
            return null;
        }

        UserInfo userInfo = new UserInfo();

        userInfo.setName( cryptoSymmentricAES.encrypt( userInfoDto.getName() ) );
        userInfo.setPhone( cryptoSymmentricAES.encrypt( userInfoDto.getPhone() ) );
        userInfo.setAge( cryptoSymmentricAES.encrypt( String.valueOf( userInfoDto.getAge() ) ) );
        userInfo.setUserId( userInfoDto.getUserId() );

        return userInfo;
    }

    @Override
    public UserInfoDto toUserInfoDto(UserInfo userInfo) {
        if ( userInfo == null ) {
            return null;
        }

        UserInfoDto userInfoDto = new UserInfoDto();

        userInfoDto.setName( cryptoSymmentricAES.decrypt( userInfo.getName() ) );
        if ( userInfo.getAge() != null ) {
            userInfoDto.setAge( Integer.parseInt( cryptoSymmentricAES.decrypt( userInfo.getAge() ) ) );
        }
        userInfoDto.setPhone( cryptoSymmentricAES.decrypt( userInfo.getPhone() ) );
        userInfoDto.setUserId( userInfo.getUserId() );

        return userInfoDto;
    }
}
