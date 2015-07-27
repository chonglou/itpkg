package com.itpkg.core.utils;

import com.itpkg.core.services.SessionService;
import com.itpkg.core.services.SettingService;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.Key;
import java.util.Base64;

/**
 * Created by flamen on 15-7-21.
 */
public abstract class JwtHelper {
    private final static Logger logger = LoggerFactory.getLogger(JwtHelper.class);

    public <T> T token2payload(String token, Class<T> clazz) throws InvalidJwtException, MalformedClaimException {
        JwtConsumer consumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setExpectedIssuer(TOKEN_ISSUER)
                .setExpectedAudience(TOKEN_AUDIENCE)
                .setVerificationKey(getVerificationKey())
                .build();

        JwtClaims claims = consumer.processToClaims(token);
        T t = jsonHelper.json2object(claims.getClaimValue(CLAIM_KEY, String.class), clazz);
        if (sessionService.getByToken(token) == null) {
            return null;
        }
        return t;

    }

    public String payload2token(String subject, Object payload, long minutes) throws JoseException {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(TOKEN_ISSUER);
        claims.setAudience(TOKEN_AUDIENCE);
        claims.setExpirationTimeMinutesInTheFuture(minutes);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setSubject(subject);
        claims.setStringClaim(CLAIM_KEY, jsonHelper.object2json(payload));

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());

        jws.setKey(getKey());
        jws.setKeyIdHeaderValue(KEY_ID);
        jws.setAlgorithmHeaderValue(getAlgorithm());


        String token = jws.getCompactSerialization();
        sessionService.saveToken(token);
        return token;

    }

    public void init() throws JoseException, IOException, ClassNotFoundException {
        String id = "site.token.key." + getAlgorithm();
        String keyS = settingService.get(id, String.class);
        if (keyS == null) {
            try (
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos)
            ) {
                Object keyO = generateKey();
                oos.writeObject(keyO);
                settingService.set(id, Base64.getEncoder().encodeToString(baos.toByteArray()), true);
                setup(keyO);
            }
        } else {
            try (
                    ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(keyS));
                    ObjectInputStream ois = new ObjectInputStream(bais);
            ) {
                setup(ois.readObject());
            }
        }
    }

    protected abstract String getAlgorithm();

    protected abstract void setup(Object o) throws JoseException;

    protected abstract Object generateKey() throws JoseException;

    protected abstract Key getVerificationKey();

    protected abstract Key getKey();


    protected final String TOKEN_ISSUER = "itpkg";
    protected final String TOKEN_AUDIENCE = "user";
    protected final String CLAIM_KEY = "data";
    protected final String KEY_ID = "itpkg-web";


    protected JsonHelper jsonHelper;
    protected SettingService settingService;
    protected SessionService sessionService;

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setSettingService(SettingService settingService) {
        this.settingService = settingService;
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

}
