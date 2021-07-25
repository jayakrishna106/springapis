package com.mytest.webapi.service.impl;

import com.mytest.webapi.model.PasswordResetToken;
import com.mytest.webapi.model.User;
import com.mytest.webapi.model.dto.PasswordResetTokenDto;
import com.mytest.webapi.model.dto.PasswordResetTokenMapper;
import com.mytest.webapi.repository.PasswordResetTokenRepository;
import com.mytest.webapi.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordResetTokenMapper passwordResetTokenMapper;

    @Override
    public PasswordResetToken generationToken(User user) {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUserId(user.getId());
        token.setUser(user);
        token.setExpiryDate(30);
        passwordResetTokenRepository.save(token);
        return token;
    }

    @Override
    public PasswordResetTokenDto findByToken(String token) {
        return passwordResetTokenMapper.toPasswordTokenDto(passwordResetTokenRepository.findByToken(token));
    }

    @Override
    public void delete(PasswordResetTokenDto tokenDto) {
        passwordResetTokenRepository.delete(passwordResetTokenMapper.toPasswordToken(tokenDto));
    }
}
