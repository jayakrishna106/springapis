package com.mytest.webapi.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtils {
    public static String digestSHA(final String password){
        String base64;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
            base64 = Base64.getEncoder().encodeToString(messageDigest.digest());
        }catch (NoSuchAlgorithmException e){
            throw  new RuntimeException(e);
        }
        return "{SHA512}" + base64;
    }
    public static String decode(String rawPassword){
        byte[] decodeBytes = Base64.getDecoder().decode(rawPassword);
        return new String(decodeBytes);
    }
}
