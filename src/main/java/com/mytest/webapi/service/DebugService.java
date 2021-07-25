package com.mytest.webapi.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class DebugService {
    @Value("${spring.debug.level}")
    private boolean debugEnabled;
    public boolean isDebugEnabled(){
        return this.debugEnabled;
    }
    public void debugLog(Class clazz, String message){
        if(this.debugEnabled){
            log.debug("{} : {}", clazz.getName(), message);
        }
    }
}
