package com.mytest.webapi.model.dto;

import com.mytest.webapi.constraint.FieldMatch;
import com.mytest.webapi.constraint.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@FieldMatch(first = "password", second = "confirmPassword",
            message = "The password fields must match")
public class UserPasswordDto {

    @ValidPassword
    private String oldPassword;
    @ValidPassword
    private String newPassword;
    @NotBlank
    private String confirmPassword;
}
