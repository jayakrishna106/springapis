package com.mytest.webapi.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    private Long id;

    @NotEmpty
    @NotNull
    private String username;
    @NotEmpty
    @NotNull
    private String email;
}
