package com.mytest.webapi.service.impl;

import com.mytest.webapi.LdapClient.LdapClient;
import com.mytest.webapi.exception.BruteForceException;
import com.mytest.webapi.model.PasswordResetToken;
import com.mytest.webapi.model.User;
import com.mytest.webapi.model.dto.*;
import com.mytest.webapi.service.*;
import com.mytest.webapi.util.IpTools;
import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResetPasswordImpl implements ResetPasswordService {
    private final RecaptchaService recaptchaService;
    private final UserService userService;
    private final LoginAttemptService loginAttemptService;
    private final IpTools ipTools;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final DebugService debugService;
    private final LdapClient ldapClient;

    @Value("${spring.mail.username}")
    private String mailAddress;

    private static final String RECAPTCHA_PARAM = "g-recaptcha-response";

    @Override
    public void sendMail(HttpServletRequest request, PasswordForgotDto passwordForgotDto, BindingResult result) {
        if (loginAttemptService.isBlocked(ipTools.getClientIP(request))){
            throw new BruteForceException("Your IP is blocked");
        }
        loginAttemptService.loginFailed(ipTools.getClientIP(request));
        String responseCaptcha = request.getParameter(RECAPTCHA_PARAM);
        RecaptchaResponseDto recaptchaResponseDto = recaptchaService.getRecaptchaResponse(responseCaptcha);
        if(result.hasErrors() || recaptchaResponseDto == null
                    || !recaptchaResponseDto.isSuccess())
            return;

        User user = userService.findByEmail(passwordForgotDto.getEmail().toLowerCase());
        if(user==null){
            result.rejectValue("email", "", "You've successfully requested a new password reset! ");
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                return;
            }
        }
        loginAttemptService.loginSucceeded(ipTools.getClientIP(request));
        PasswordResetToken token = tokenService.generationToken(user);
        String url = String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort());
        Map<String, Object> model = new HashMap<String, Object>(){
            {
                put("token", token);
                put("user", user);
                put("signature", "advertisements");
                put("resetUrl", url + "/reset-password?token=" + token.getToken());
            }
        };

        MailDto mailDto = new MailDto().setTo(user.getEmail())
                                    .setFrom(mailAddress)
                                    .setSubject("Password reset request")
                                    .setModel(model);

        emailService.sendEmail(mailDto);

        log.info("{} wants to reset password", user.getId());
        result.rejectValue("email", "error", "You've successfully requested a new password reset! ");

    }

    @Override
    @Transactional
    public void resetPassword(PasswordResetDto passwordResetDto) {
        PasswordResetTokenDto tokenDto = tokenService.findByToken(passwordResetDto.getToken());
        User user = tokenDto.getUser();
        userService.updatePassword(passwordResetDto.getPassword(), user.getId());
        debugService.debugLog(this.getClass(), String.format("User %s updating password", user.getId()));
        ldapClient.modify(user.getEmail().toLowerCase(), passwordResetDto.getPassword() );
        debugService.debugLog(this.getClass(), String.format("User %s updating password success", user.getId()));
        tokenService.delete(tokenDto);
    }
}
