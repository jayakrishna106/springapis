package com.mytest.webapi.model.dto;

import com.mytest.webapi.model.User;
import lombok.Data;

import java.util.Date;

@Data
public class PasswordResetTokenDto {
private Long id;
private String token;
private User user;
private Date expiryDates;

public boolean isExpired(){
    return new Date().after(this.expiryDates);
}
}
