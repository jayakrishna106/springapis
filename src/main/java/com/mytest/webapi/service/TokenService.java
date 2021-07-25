package com.mytest.webapi.service;

import com.mytest.webapi.model.PasswordResetToken;
import com.mytest.webapi.model.User;
import com.mytest.webapi.model.dto.PasswordResetTokenDto;

public interface TokenService {
    PasswordResetToken generationToken(User user);
    PasswordResetTokenDto findByToken(String token);
    void delete(PasswordResetTokenDto tokenDto);
}
