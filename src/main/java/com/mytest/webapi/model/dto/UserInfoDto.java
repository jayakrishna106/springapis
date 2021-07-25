package com.mytest.webapi.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Data
public class UserInfoDto {

    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    private int age;
    @NotNull
    @Length(min = 11, max = 11)
    private String phone;

    private Long userId;
}
