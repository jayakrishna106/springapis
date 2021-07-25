package com.mytest.webapi.util;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@NoArgsConstructor
public class IpTools {
    public String getClientIP(HttpServletRequest request){
        String remoteAddr = request.getHeader("X-Forwarded-For");

        if(remoteAddr!=null && remoteAddr.split(",").length !=0)
            return remoteAddr.split(",")[0];

    remoteAddr = request.getRemoteAddr();
    return remoteAddr;
    }

}
