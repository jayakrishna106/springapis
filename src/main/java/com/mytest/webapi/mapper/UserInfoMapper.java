package com.mytest.webapi.mapper;

import com.mytest.webapi.model.UserInfo;
import com.mytest.webapi.model.dto.UserInfoDto;
import com.mytest.webapi.security.encryption.AES.CryptoSymmentricAES;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CryptoSymmentricAES.class)
public interface UserInfoMapper {
    UserInfo toUserInfo(UserInfoDto userInfoDto);
    UserInfoDto toUserInfoDto(UserInfo userInfo);
}
