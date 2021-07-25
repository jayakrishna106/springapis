package com.mytest.webapi.service;

import com.mytest.webapi.model.dto.UserInfoDto;

import java.io.File;

public interface UserInfoSerializeService {
    void serializeUserInfo(UserInfoDto userInfoDto, File file);
    UserInfoDto deserialize(File file);
}
