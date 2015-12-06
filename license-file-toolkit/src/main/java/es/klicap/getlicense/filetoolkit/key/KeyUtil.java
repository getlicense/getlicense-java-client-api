/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: KeyUtil.java 343 2015-03-02 08:39:43Z amuniz $
 */
package es.klicap.getlicense.filetoolkit.key;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 * This is an utility class to encode and decode keys using DSA algorithm.
 */
public class KeyUtil {

    /**
     * Key pairs factory
     */
    private static KeyFactory kf;

    static {
        try {
            kf = KeyFactory.getInstance("DSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * The method gets the public key from the encoded bytes.
     * 
     * The ASN.1 public key format is used, encoded according to the ASN.1 type <code>SubjectPublicKeyInfo</code>.
     * The <code>SubjectPublicKeyInfo</code> syntax is defined in the X.509 standard.
     */
    public static PublicKey getPublic(final byte[] encodedKey) throws InvalidKeySpecException {
        return kf.generatePublic(new X509EncodedKeySpec(encodedKey));
    }

    /**
     * This method gets the private key from the encoded byte.
     * 
     * The ASN.1 private key format is used, encoded according to the ASN.1 type <code>PrivateKeyInfo</code>.
     * The <code>PrivateKeyInfo</code> syntax is defined in the PKCS#8 standard
     */
    public static PrivateKey getPrivate(final byte[] encodedKey) throws InvalidKeySpecException {
        return kf.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    /**
     * Generates a secure (1024 bits with 8 bytes seed) public/private key pair.
     */
    public static StringKeyPair generateKeyPair() {
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance("DSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
        SecureRandom sr = new SecureRandom();
        kpg.initialize(1024, new SecureRandom(sr.generateSeed(8)));
        KeyPair pair = kpg.generateKeyPair();
        return new StringKeyPair(new String(Hex.encodeHex(pair.getPublic().getEncoded())), new String(
                Hex.encodeHex(pair.getPrivate().getEncoded())));
    }

}
