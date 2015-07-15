package com.itpkg.core.utils;

import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.Key;

/**
 * Created by flamen on 15-7-15.
 */
@Component
public class EncryptHelper {

    public <T> T token2payload(String token, Class<T> clazz) throws JoseException, IOException {
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setKey(tokenKey);
        jwe.setCompactSerialization(token);
        return jsonHelper.json2object(jwe.getPayload(), clazz);
    }

    public String payload2token(Object payload) throws JoseException, IOException {
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setPayload(jsonHelper.object2json(payload));
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.PBES2_HS512_A256KW);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_CBC_HMAC_SHA_512);
        jwe.setKey(tokenKey);
        return jwe.getCompactSerialization();
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

        tokenKey = new AesKey(tokenKeyS.getBytes());
    }

    @Value("${secret.encryptor}")
    String encryptorKey;
    @Value("${secret.token}")
    String tokenKeyS;
    @Autowired
    JsonHelper jsonHelper;


    private PasswordEncryptor passwordEncryptor;
    private StrongTextEncryptor textEncryptor;
    private Key tokenKey;

}
