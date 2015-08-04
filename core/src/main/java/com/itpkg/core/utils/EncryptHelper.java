package com.itpkg.core.utils;

import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;

/**
 * Created by flamen on 15-7-15.
 */
@Component("core.utils.encrypt")
public class EncryptHelper {

    public String toBase64(String plain) {

        return Base64.getEncoder().encodeToString(plain.getBytes());

    }

    public String toBase64(byte[] plain) {

        return Base64.getEncoder().encodeToString(plain);

    }


    public String fromBase64(String encode) {

        return new String(Base64.getDecoder().decode(encode));

    }

    public String encrypt(String plain) {
        return textEncryptor.encrypt(plain);
    }

    public String decrypt(String encode) {
        return textEncryptor.decrypt(encode);
    }

    public String password(String plain) {
        return passwordEncryptor.encryptPassword(plain);

    }

    public boolean check(String plain, String encode) {
        return passwordEncryptor.checkPassword(plain, encode);
    }

    @PostConstruct
    void init() {
        passwordEncryptor = new StrongPasswordEncryptor();
        textEncryptor = new StrongTextEncryptor();
        textEncryptor.setPassword(encryptorKey);

    }

    @Value("${secrets.encryptor}")
    String encryptorKey;
    @Autowired
    JsonHelper jsonHelper;


    private PasswordEncryptor passwordEncryptor;
    private StrongTextEncryptor textEncryptor;

}
