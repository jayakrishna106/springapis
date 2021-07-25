package com.mytest.webapi.model.dto;

import com.mytest.webapi.constraint.FieldMatch;
import com.mytest.webapi.constraint.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@FieldMatch(first = "password", second = "confirmPassword",
message = "The password fields must match")
public class PasswordResetDto {
    @ValidPassword
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotEmpty
    private String token;
}
