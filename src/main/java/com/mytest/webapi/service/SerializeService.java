package com.mytest.webapi.service;

import com.mytest.webapi.model.dto.UserInfoDto;

import java.util.UUID;

public interface SerializeService {

    UUID serialize(UserInfoDto userInfoDto);
    UserInfoDto deserialize(UUID uuid);
}
