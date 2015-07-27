package com.itpkg.core.utils.impl;

import com.itpkg.core.utils.JwtHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;

import java.security.Key;

/**
 * Created by flamen on 15-7-27.
 */
public class JwtHS512Helper extends JwtHelper {


    @Override
    protected String getAlgorithm() {
        return AlgorithmIdentifiers.HMAC_SHA512;
    }

    @Override
    protected void setup(Object o) throws JoseException {
        key = new AesKey(((String) o).getBytes());
    }

    @Override
    protected Object generateKey() throws JoseException {
        return RandomStringUtils.random(32);
    }

    @Override
    protected Key getVerificationKey() {
        return key;
    }

    @Override
    protected Key getKey() {
        return key;
    }

    private AesKey key;
}
