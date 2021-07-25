package com.mytest.webapi.service.impl;

import com.mytest.webapi.LdapClient.LdapClient;
import com.mytest.webapi.model.User;
import com.mytest.webapi.model.dto.RecaptchaResponseDto;
import com.mytest.webapi.model.dto.UserRegistrationDto;
import com.mytest.webapi.service.RecaptchaService;
import com.mytest.webapi.service.RegistrationService;
import com.mytest.webapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.management.relation.RoleNotFoundException;
import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final RecaptchaService recaptchaService;
    private final UserService userService;
    private final LdapClient ldapClient;

    private static final String RECAPTCHA_PARAM = "g-recaptcha-response";

    public boolean register(HttpServletRequest request, UserRegistrationDto userRegistrationDto,
                            BindingResult result)throws RoleNotFoundException {
        String responseCaptcha = request.getParameter(RECAPTCHA_PARAM);
        User existingUser = userService.findByEmail(userRegistrationDto.getEmail().toLowerCase());
        if(existingUser !=null && ldapClient.search(userRegistrationDto)){
            result.rejectValue("email", "", "There is already an account registered with the email.");
        }
        RecaptchaResponseDto  recaptchaResponseDto = recaptchaService.getRecaptchaResponse(responseCaptcha);

        if(result.hasErrors() || (recaptchaResponseDto != null && !recaptchaResponseDto.isSuccess()))
            return false;

        userService.save(userRegistrationDto);
        ldapClient.create(userRegistrationDto);
        return true;
    }
}
