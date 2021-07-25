package com.mytest.webapi.service;

import com.mytest.webapi.model.dto.UserRegistrationDto;
import org.springframework.validation.BindingResult;

import javax.management.relation.RoleNotFoundException;
import javax.servlet.http.HttpServletRequest;

public interface RegistrationService {
    boolean register(HttpServletRequest request, UserRegistrationDto userRegistrationDto,
                     BindingResult result) throws RoleNotFoundException;
}
