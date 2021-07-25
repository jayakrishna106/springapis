package com.mytest.webapi.util;

import com.mytest.webapi.exception.HashFileException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class HashFileSerialized {
    private final String HASH_ALGORITHM = "SHA-512";
    public String hashFile(File file){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            FileInputStream fis = new FileInputStream(file);
            byte[] dataBytes = new byte[1024];
            int nread = 0;
            while ((nread = fis.read(dataBytes))!= -1){
                messageDigest.update(dataBytes,0,nread);
            }
            byte[] bytes = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return stringBuilder.toString();
        }catch (NoSuchAlgorithmException | IOException e){
            throw new HashFileException("Calculate hash file failed");
        }
    }
}
