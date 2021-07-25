package com.mytest.webapi.model.dto;

import com.mytest.webapi.constraint.EmailValidate;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class PasswordForgotDto {
    @Pattern(regexp = EmailValidate.REGEX_EMAIL_VALIDATION,message = "Invalid email")
    private String email;
}
