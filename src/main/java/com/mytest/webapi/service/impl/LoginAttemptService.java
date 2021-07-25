package com.mytest.webapi.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {
    @Value("${protection.timeout.numberofattemts}")
    private int MAX_ATTEMPT;

    @Value("${protection.timeout.minutes}")
    private int LOCK_TIME_MINUTES;

    private LoadingCache<String, Integer> attemptCache;

    public LoginAttemptService(){
        super();
        attemptCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS)
                        .build(new CacheLoader<String, Integer>() {
                            @Override
                            public Integer load(String s) throws Exception {
                                return 0;
                            }
                        });
    }
    public  void loginSucceeded(String key){
        attemptCache.invalidate(key);
    }
    public void loginFailed(String key){
        int attempts = attemptCache.getUnchecked(key);
        attempts ++;
        attemptCache.put(key, attempts);
    }

    public boolean isBlocked(String key){
        return attemptCache.getUnchecked(key) >= MAX_ATTEMPT;
    }
}
