package com.mytest.webapi.service;

import com.mytest.webapi.model.dto.RecaptchaResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class RecaptchaService {
    private final RestTemplate restTemplate;

    @Value("${recaptcha.url}")
    private String recaptchaUrl;

    @Value("${recaptcha.key.secret}")
    private String recaptchaSecret;
    @Value("${recaptcha.key.public}")
    private String recaptchaPublic;

    public RecaptchaResponseDto getRecaptchaResponse(String responseCaptcha){
        String url = String.format(recaptchaUrl + "?secret=%s&response=%s",recaptchaSecret,responseCaptcha);
        return restTemplate.postForObject(url, Collections.emptyList(),RecaptchaResponseDto.class);
    }

    public String getRecaptchaPublic(){
        return this.recaptchaPublic;
    }
}
