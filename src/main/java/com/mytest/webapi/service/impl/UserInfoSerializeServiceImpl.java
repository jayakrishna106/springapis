package com.mytest.webapi.service.impl;

import com.mytest.webapi.exception.SerializeException;
import com.mytest.webapi.model.dto.UserInfoDto;
import com.mytest.webapi.security.encryption.AES.CryptoSymmentricAES;
import com.mytest.webapi.service.UserInfoSerializeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.serialization.ValidatingObjectInputStream;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;

@Service
@RequiredArgsConstructor
public class UserInfoSerializeServiceImpl implements UserInfoSerializeService {
    private final CryptoSymmentricAES cryptoSymmentricAES;

    @Override
    public void serializeUserInfo(UserInfoDto userInfoDto, File file) {
        try (
                FileOutputStream out = new FileOutputStream(file);
                CipherOutputStream cipherOutputStream = new CipherOutputStream(out, cryptoSymmentricAES.getCipher(Cipher.ENCRYPT_MODE))
        ){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(cipherOutputStream);
            objectOutputStream.writeObject(userInfoDto);
            objectOutputStream.close();
        }catch (IOException e){
            throw new SerializeException("Serialize object failed");
        }
    }

    @Override
    public UserInfoDto deserialize(File file) {
        try (FileInputStream in = new FileInputStream(file)){
            CipherInputStream cipherInputStream = new CipherInputStream(in, cryptoSymmentricAES.getCipher(Cipher.DECRYPT_MODE));
            ValidatingObjectInputStream validatingObjectInputStream = new ValidatingObjectInputStream(cipherInputStream);
            UserInfoDto userInfoDto = (UserInfoDto) validatingObjectInputStream.readObject();
            validatingObjectInputStream.close();
            return userInfoDto;
        }
        catch (IOException | ClassNotFoundException e){
            throw new SerializeException("Deserialize object failed");
        }
    }
}
