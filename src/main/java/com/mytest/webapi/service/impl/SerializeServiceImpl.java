package com.mytest.webapi.service.impl;

import com.mytest.webapi.model.HashObject;
import com.mytest.webapi.model.User;
import com.mytest.webapi.model.dto.UserInfoDto;
import com.mytest.webapi.repository.SerializeObjectRepository;
import com.mytest.webapi.service.SerializeService;
import com.mytest.webapi.service.UserInfoSerializeService;
import com.mytest.webapi.service.UserService;
import com.mytest.webapi.util.HashFileSerialized;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SerializeServiceImpl implements SerializeService {
    @Value("${serialize.service.object.basedir}")
    private String objectDir;

    private UserService userService;
    private HashFileSerialized hashFileSerialized;
    private UserInfoSerializeService userInfoSerializeService;
    private SerializeObjectRepository serializeObjectRepository;

    @Override
    public UUID serialize(UserInfoDto userInfoDto) {
        User user = userService.getContextUser();
        userInfoDto.setUserId(user.getId());
        UUID fileId = UUID.randomUUID();
        File serializeFile = new File(objectDir, fileId.toString());
        userInfoSerializeService.serializeUserInfo(userInfoDto, serializeFile);
        String hashValue = hashFileSerialized.hashFile(serializeFile);
        serializeObjectRepository.save(new HashObject(fileId, hashValue));
        return fileId;
    }

    @Override
    public UserInfoDto deserialize(UUID uuid) {
        File deserializeSourceFile = new File(objectDir, uuid.toString());
        HashObject hashObject = serializeObjectRepository.getOne(uuid);
        if(!hashObject.getHashValue().equals(hashFileSerialized.hashFile(deserializeSourceFile)))
            return null;

        return userInfoSerializeService.deserialize(deserializeSourceFile);
    }
}
