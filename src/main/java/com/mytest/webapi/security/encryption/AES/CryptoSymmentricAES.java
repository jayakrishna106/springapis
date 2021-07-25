package com.mytest.webapi.security.encryption.AES;

import com.mytest.webapi.service.CipherService;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;

@Log4j2
@Service
public class CryptoSymmentricAES implements CipherService {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH= 16;
    private static final int GCM_IV_LENGTH = 12;

    @Value("${service.cipher.keyStoragePassword}")
    private String keyStorePassword;
    @Value("${service.cipher.encryptPassword}")
    private String encryptPassword;
    @Value("${service.cipher.aliasKey}")
    private String aliasKey;
    @Value("${service.cipher.pathKeyStorage}")
    private String pathKeyStorage;
    @Value("${service.cipher.typeKey}")
    private String typeKey;

    private SecretKey secretKey;
    private byte[] iv = new byte[12];

    @PostConstruct
    public void initialize() {
        try {
            KeyStore keyStore = KeyStore.getInstance(typeKey);
            keyStore.load(new FileInputStream(new File(pathKeyStorage)), keyStorePassword.toCharArray());
            if (keyStore.containsAlias(aliasKey)) {
                this.secretKey = (SecretKey) keyStore.getKey(aliasKey, encryptPassword.toCharArray());
                (new SecureRandom()).nextBytes(iv);
            } else {
                throw new IllegalStateException("Invalid keyStore");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Generate secret key failed.");
        }
    }

    public byte[] encrypt(String val){
        try {
            byte[] ciphertext = null;
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] initVector = new byte[GCM_IV_LENGTH];
            (new SecureRandom()).nextBytes(initVector);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, initVector);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
            byte[] encoded = val.getBytes(StandardCharsets.UTF_8);
            ciphertext = new byte[initVector.length + cipher.getOutputSize(encoded.length)];
            for(int i = 0; i < initVector.length; i++){
                ciphertext[i] = initVector[i];
            }
            cipher.doFinal(encoded,0,encoded.length,ciphertext,initVector.length);
            return ciphertext;
        }catch (Exception e){
            throw new IllegalStateException("Encryption Failed");
        }
    }
    @Override
    public String decrypt(byte[] value){
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] initVector = Arrays.copyOfRange(value,0,GCM_IV_LENGTH);
            GCMParameterSpec  spec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, initVector);
            cipher.init(Cipher.DECRYPT_MODE, secretKey,spec);
            byte[] plaintext = cipher.doFinal(value,
                    GCM_IV_LENGTH,
                    value.length - GCM_IV_LENGTH);
            return new String(plaintext, StandardCharsets.UTF_8);
        }catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e){
            throw new IllegalStateException("Decryption Failed.");
        }
    }

    public Cipher getCipher(int mode){
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, this.iv);
            cipher.init(mode, secretKey,gcmSpec);
            return cipher;
        }catch (NoSuchAlgorithmException | NoSuchPaddingException| InvalidKeyException
                | InvalidAlgorithmParameterException e){
            throw new IllegalStateException("Get cipher failed", e);
        }
    }
}
