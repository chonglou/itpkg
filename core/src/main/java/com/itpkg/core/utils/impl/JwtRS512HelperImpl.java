package com.itpkg.core.utils.impl;

import com.itpkg.core.utils.JwtHelper;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.keys.RsaKeyUtil;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.security.KeyPair;

/**
 * Created by flamen on 15-7-27.
 */
public class JwtRS512HelperImpl extends JwtHelper {
    private final static Logger logger = LoggerFactory.getLogger(JwtRS512HelperImpl.class);


    @Override
    protected String getAlgorithm() {
        return AlgorithmIdentifiers.RSA_USING_SHA512;
    }

    @Override
    protected void setup(Object o) throws JoseException {
        KeyPair kp = (KeyPair) o;

        key = (RsaJsonWebKey) PublicJsonWebKey.Factory.newPublicJwk(kp.getPublic());
        key.setPrivateKey(kp.getPrivate());
        key.setKeyId("itpkg-web");
    }

    @Override
    protected Object generateKey() throws JoseException {
        RsaKeyUtil keyUtil = new RsaKeyUtil();
        return keyUtil.generateKeyPair(2048);
    }

    @Override
    protected Key getVerificationKey() {
        return key.getKey();
    }

    @Override
    protected Key getKey() {
        return key.getPrivateKey();
    }

    private RsaJsonWebKey key;
}
