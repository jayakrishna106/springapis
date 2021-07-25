package com.mytest.webapi.service;

import javax.crypto.Cipher;

public interface CipherService {
    byte[] encrypt(String value);
    String decrypt(byte[] value);
    Cipher getCipher(int mode);
}
