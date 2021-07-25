package com.mytest.webapi.service;

import com.mytest.webapi.model.dto.PasswordForgotDto;
import com.mytest.webapi.model.dto.PasswordResetDto;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public interface ResetPasswordService {
    void sendMail(HttpServletRequest request, PasswordForgotDto passwordForgotDto, BindingResult result);
    void resetPassword(PasswordResetDto passwordResetDto);
}
