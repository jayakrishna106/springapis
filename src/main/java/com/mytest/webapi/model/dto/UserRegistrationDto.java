package com.mytest.webapi.model.dto;

import com.mytest.webapi.constraint.EmailValidate;
import com.mytest.webapi.constraint.FieldMatch;
import com.mytest.webapi.constraint.ValidPassword;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword",
                    message = "The password fields must match"),
        @FieldMatch(first = "email", second = "confirmEmail",
                    message = "The email fields must match")
})
public class UserRegistrationDto {
    @ValidPassword
    private String password;

    @NotEmpty
    private String confirmPassword;

    @Pattern(regexp = EmailValidate.REGEX_EMAIL_VALIDATION, message = "Invalid email")
    private String email;

    @NotEmpty
    private String confirmEmail;

    @AssertTrue
    private Boolean terms;
}
