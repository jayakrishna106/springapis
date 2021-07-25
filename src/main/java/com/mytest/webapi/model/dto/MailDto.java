package com.mytest.webapi.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class MailDto {
    private String from;
    private String to;
    private String subject;
    private Map<String, Object> model;
}
