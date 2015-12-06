/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: JsonLicenseReader.java 322 2015-02-23 19:10:44Z amuniz $
 */
package es.klicap.getlicense.filetoolkit.json;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Map;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.ParseException;

import es.klicap.getlicense.filetoolkit.License;
import es.klicap.getlicense.filetoolkit.LicenseReader;
import es.klicap.getlicense.filetoolkit.exception.LicenseFormatException;
import es.klicap.getlicense.filetoolkit.impl.DefaultLicense;
import es.klicap.getlicense.filetoolkit.key.KeyUtil;

/**
 * License reader in JSON format.
 *
 * <pre>
 * JsonLicenseReader reader = null;
 * try {
 *     reader = new JsonLicenseReader(jsonLicense, publicKey);
 * } catch (LicenseFormatException e) {
 *     // License format is not valid. Perhaps it's not a JSON.
 * }
 *
 * if(reader.isSignatureValid()) {
 *     Map<String, String> features = reader.getLicense().getFeatures();
 *     // do stuff with features
 * }
 * </pre>
 *
 */
public class JsonLicenseReader implements LicenseReader {

    /**
     * Singature key
     */
    protected static final String SIGNATURE_KEY = "Signature";

    /**
     * Sign algorithm.
     */
    protected static final String SIGN_ALGORITHM = "DSA";

    /**
     * License.
     */
    protected DefaultLicense license;

    /**
     * Signature.
     */
    protected String signature;

    /**
     * Public key to validate the license against.
     */
    protected String publicKey;

    /**
     * Only used by subclasses.
     */
    protected JsonLicenseReader(final String publicKey) {
        this.publicKey = publicKey;
        this.license = new DefaultLicense();
    }

    /**
     * Constructor.
     *
     * @param licenseAsString
     * @param publicKey
     * @throws LicenseFormatException
     */
    public JsonLicenseReader(final String licenseAsString, final String publicKey) throws LicenseFormatException {
        this(licenseAsString, publicKey, false);
    }

    /**
     * Read a license given as JSON string.
     */
    public JsonLicenseReader(final String licenseAsString, final String publicKey, final boolean isBase64)
            throws LicenseFormatException {
        this.license = new DefaultLicense();
        this.publicKey = publicKey;
        String decoded = licenseAsString;
        if (isBase64) {
            decoded = new String(Base64.decodeBase64(licenseAsString));
        }

        JsonObject parser;
        try {
            parser = JsonObject.readFrom(decoded);
        } catch (ParseException e) {
            throw new LicenseFormatException("Invalid format for license", e);
        }
        for (Member m : parser) {
            String name = m.getName();
            if (name.equals(SIGNATURE_KEY)) {
                signature = m.getValue().asString();
            } else {
                license.addFeature(m.getName(), m.getValue().asString());
            }
        }
    }

    /* (non-Javadoc)
     * @see es.klicap.getlicense.filetoolkit.LicenseReader#getLicense()
     */
    @Override
    public License getLicense() {
        return license;
    }

    /* (non-Javadoc)
     * @see es.klicap.getlicense.filetoolkit.LicenseReader#isSignatureValid()
     */
    @Override
    public boolean isSignatureValid() {
        try {
            return this.verify();
        } catch (GeneralSecurityException e) {
            // TODO: log here
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method validates license data based on the signature and the public key.
     */
    private boolean verify() throws GeneralSecurityException {
        if (signature != null) {
            Signature sig = Signature.getInstance(SIGN_ALGORITHM);
            byte[] decodeHex;
            byte[] publicKeyBytes;
            try {
                publicKeyBytes = Hex.decodeHex(publicKey.toCharArray());
                decodeHex = Hex.decodeHex(signature.toCharArray());
            } catch (DecoderException e) {
                // TODO: log here
                e.printStackTrace();
                return false;
            }

            PublicKey key = KeyUtil.getPublic(publicKeyBytes);
            sig.initVerify(key);
            sig.update(asJson(false).getBytes());
            return sig.verify(decodeHex);

        } else {
            return false;
        }
    }

    /**
     * Returns the JSON representation of the license including the signature field.
     */
    protected String asJson() {
        return asJson(true);
    }

    /**
     * Returns the JSON representation of the license including the signature field.
     * If includeSignature is true the signature field will be included.
     */
    protected String asJson(final boolean includeSignature) {
        JsonObject jsonObj = new JsonObject();
        Map<String, String> features = this.license.getFeatures();
        for (String name : features.keySet()) {
            jsonObj.add(name, features.get(name));
        }
        if (includeSignature) {
            jsonObj.add(SIGNATURE_KEY, this.signature);
        }
        return jsonObj.toString();
    }
}
