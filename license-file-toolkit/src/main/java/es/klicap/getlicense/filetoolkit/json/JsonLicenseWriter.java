/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: JsonLicenseWriter.java 322 2015-02-23 19:10:44Z amuniz $
 */
package es.klicap.getlicense.filetoolkit.json;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import es.klicap.getlicense.filetoolkit.LicenseWriter;
import es.klicap.getlicense.filetoolkit.exception.InvalidKeysException;
import es.klicap.getlicense.filetoolkit.exception.LicenseFormatException;
import es.klicap.getlicense.filetoolkit.key.KeyUtil;

/**
 * JSON license format implementation.
 *
 * <pre>
 * LicenseWriter writer = new JsonLicenseWriter(publicKey, privateKey);
 * writer.addFeature("Users", "5");
 * String licenseText = writer.write();
 * </pre>
 */
public class JsonLicenseWriter extends JsonLicenseReader implements LicenseWriter {

    /**
     * Mimetype for license file.
     */
    public static final String MIMETYPE = "application/octet-stream";

    /**
     * Default file name.
     */
    public static final String FILENAME = "license.json";

    /**
     * Private key.
     */
    private String privateKey;

    /**
     * Full constructor.
     */
    public JsonLicenseWriter(final String licenseAsString, final String publicKey, final String privateKey)
            throws LicenseFormatException {
        super(licenseAsString, publicKey);
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * Partial constructor using public and private key.
     */
    public JsonLicenseWriter(final String publicKey, final String privateKey) {
        super(publicKey);
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /* (non-Javadoc)
     * @see es.klicap.getlicense.filetoolkit.LicenseWriter#addFeature(java.lang.String, java.lang.String)
     */
    @Override
    public LicenseWriter addFeature(final String feature, final String value) {
        license.addFeature(feature, value);
        return this;
    }

    /* (non-Javadoc)
     * @see es.klicap.getlicense.filetoolkit.LicenseWriter#setFeature(java.lang.String, java.lang.String)
     */
    @Override
    public LicenseWriter setFeature(final String feature, final String value) {
        license.addFeature(feature, value);
        return this;
    }

    /* (non-Javadoc)
     * @see es.klicap.getlicense.filetoolkit.LicenseWriter#deleteFeature(java.lang.String)
     */
    @Override
    public LicenseWriter deleteFeature(final String feature) {
        license.deleteFeature(feature);
        return this;
    }

    /* (non-Javadoc)
     * @see es.klicap.getlicense.filetoolkit.LicenseWriter#write()
     */
    @Override
    public String write() throws InvalidKeysException {
        if (!isSignatureValid()) {
            updateSignature();
        }
        return asJson();
    }

    /* (non-Javadoc)
     * @see es.klicap.getlicense.filetoolkit.LicenseWriter#writeB64()
     */
    @Override
    public String writeB64() throws InvalidKeysException {
        return Base64.encodeBase64String(this.write().getBytes());
    }

    /**
     * Update the signature given the current license content.
     */
    private void updateSignature() throws InvalidKeysException {
        try {
            if (privateKey != null) {
                this.sign();
            } else {
                throw new InvalidKeysException("A private key is needed to sign");
            }
        } catch (InvalidKeyException e) {
            // Private key is not valid
            throw new InvalidKeysException("Invalid private key", e);
        } catch (NoSuchAlgorithmException e) {
            // Can not use DSA as sign algorithm
            throw new InvalidKeysException("Invalid sign algorithm", e);
        } catch (InvalidKeySpecException e) {
            // Private key is not valid
            throw new InvalidKeysException("Invalid private key", e);
        } catch (SignatureException e) {
            // JVM sign request error
            throw new InvalidKeysException("Can not perform signed", e);
        } catch (DecoderException e) {
            // HEX conversion error
            throw new InvalidKeysException("Can not decode HEX private key", e);
        }
    }

    /**
     * Compute sign and update this.signature.
     */
    private void sign() throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException,
        InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(SIGN_ALGORITHM);
        PrivateKey key = KeyUtil.getPrivate(Hex.decodeHex(privateKey.toCharArray()));
        sig.initSign(key);
        sig.update(asJson(false).getBytes());
        byte[] result = sig.sign();
        this.signature = new String(Hex.encodeHex(result));
    }

}
