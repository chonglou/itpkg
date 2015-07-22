package com.itpkg.core.utils;

import com.itpkg.core.services.SettingService;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.RsaKeyUtil;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.KeyPair;

/**
 * Created by flamen on 15-7-21.
 */
@Component("core.utils.jwt")
public class JwtHelper {
    private final static Logger logger = LoggerFactory.getLogger(JwtHelper.class);

    public <T> T token2payload(String token, Class<T> clazz) {
        JwtConsumer consumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setExpectedIssuer(TOKEN_ISSUER)
                .setExpectedAudience(TOKEN_AUDIENCE)
                .setVerificationKey(key.getKey())
                .build();
        try {
            JwtClaims claims = consumer.processToClaims(token);
            return jsonHelper.json2object(claims.getClaimValue("data", String.class), clazz);
        } catch (InvalidJwtException | MalformedClaimException e) {
            logger.error("parse jwt error", e);
        }
        return null;
    }

    public String payload2token(String subject, Object payload, long minutes) {
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

        jws.setKey(key.getPrivateKey());
        jws.setKeyIdHeaderValue(key.getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA512);

        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            logger.error("generate jwt error", e);
        }
        return null;
    }

    @PostConstruct
    void init() throws JoseException {
        final String kk = "site.token.key_pair";
        KeyPair kp = settingService.get(kk, KeyPair.class);
        if (kp == null) {
            RsaKeyUtil keyUtil = new RsaKeyUtil();
            kp = keyUtil.generateKeyPair(2048);
            settingService.set(kk, kp, true);
        }

        key = (RsaJsonWebKey) PublicJsonWebKey.Factory.newPublicJwk(kp.getPublic());
        key.setPrivateKey(kp.getPrivate());
        key.setKeyId("itpkg-web");
    }


    @Autowired
    JsonHelper jsonHelper;
    @Autowired
    SettingService settingService;
    private RsaJsonWebKey key;


    private final String TOKEN_ISSUER = "itpkg";
    private final String TOKEN_AUDIENCE = "user";
    private final String CLAIM_KEY = "data";
}
