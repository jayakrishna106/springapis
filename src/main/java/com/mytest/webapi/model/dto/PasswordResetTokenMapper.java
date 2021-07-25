package com.mytest.webapi.model.dto;

import com.mytest.webapi.model.PasswordResetToken;
import org.mapstruct.Mapper;

@Mapper
public interface PasswordResetTokenMapper {
    PasswordResetTokenDto toPasswordTokenDto(PasswordResetToken token);
    PasswordResetToken toPasswordToken(PasswordResetTokenDto token);
}
