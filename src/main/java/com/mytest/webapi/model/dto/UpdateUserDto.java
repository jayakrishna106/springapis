package com.mytest.webapi.model.dto;

import com.mytest.webapi.constraint.EmailValidate;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UpdateUserDto {
    @NotNull
    @NotEmpty
    @Pattern(regexp = EmailValidate.REGEX_EMAIL_VALIDATION)
    @Length(min = 9, max = 100)
    private String oldUserMail;

    @NotNull
    @NotEmpty
    @Pattern(regexp = EmailValidate.REGEX_EMAIL_VALIDATION)
    @Length(min = 9, max = 100)
    private String newUserMail;
}
