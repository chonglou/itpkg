package com.itpkg.core.utils;

import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
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
@Component("core.utils.encrypt")
public class EncryptHelper {

    private final String TOKEN_ISSUER = "itpkg";
    private final String TOKEN_AUDIENCE = "user";

    public <T> T token2payload(String token, Class<T> clazz) throws JoseException, IOException, InvalidJwtException {
//        new JwtConsumerBuilder()
//                .setRequireExpirationTime()
//                .setAllowedClockSkewInSeconds(30)
//                .setRequireSubject()
//                .setExpectedIssuer(TOKEN_ISSUER)
//                .setExpectedAudience(TOKEN_AUDIENCE)
//                .setVerificationKey(tokenKey)
//                .build()
//                .processToClaims(token);
        JsonWebSignature jws = new JsonWebSignature();
        jws.setCompactSerialization(token);
        jws.setKey(tokenKey);
        return jws.verifySignature() ? jsonHelper.json2object(jws.getPayload(), clazz) : null;
    }

    public String payload2token(String subject, Object payload, long minutes) throws JoseException, IOException {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(TOKEN_ISSUER);
        claims.setAudience(TOKEN_AUDIENCE);
        claims.setExpirationTimeMinutesInTheFuture(minutes);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setSubject(subject);


        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(jsonHelper.object2json(payload));
        jws.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.PBES2_HS512_A256KW);
        jws.setKey(tokenKey);
        return jws.getCompactSerialization();

//        JsonWebEncryption jwe = new JsonWebEncryption();
//        jwe.setPayload(jsonHelper.object2json(payload));
//        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.PBES2_HS512_A256KW);
//        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_CBC_HMAC_SHA_512);
//        jwe.setKey(tokenKey);
//
//        return jwe.getCompactSerialization();
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

    @Value("${secrets.encryptor}")
    String encryptorKey;
    @Value("${secrets.token}")
    String tokenKeyS;
    @Autowired
    JsonHelper jsonHelper;


    private PasswordEncryptor passwordEncryptor;
    private StrongTextEncryptor textEncryptor;
    private Key tokenKey;

}
