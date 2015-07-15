package com.itpkg.core.utils;

import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Formatter;

/**
 * Created by flamen on 15-7-15.
 */
@Component
public class EncryptHelper {

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
        textEncryptor.setPassword(key);
    }

    private String hex(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    @Value("${site.secret}")
    private String key;

    private PasswordEncryptor passwordEncryptor;
    private StrongTextEncryptor textEncryptor;

    public void setKey(String key) {
        this.key = key;
    }
}
