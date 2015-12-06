/*
 * getlicense.io
 * Copyright (C) 2015 klicap - ingeniería del puzle
 *
 * $Id: PropertiesLicenseReader.java 350 2015-03-12 21:13:21Z recena $
 */
package es.klicap.getlicense.filetoolkit.props;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import es.klicap.getlicense.filetoolkit.License;
import es.klicap.getlicense.filetoolkit.LicenseReader;
import es.klicap.getlicense.filetoolkit.exception.LicenseFormatException;
import es.klicap.getlicense.filetoolkit.impl.DefaultLicense;
import es.klicap.getlicense.filetoolkit.key.KeyUtil;

/**
 * License reader in <a href="http://docs.oracle.com/javase/7/docs/api/java/util/Properties.html">Properties</a> format.
 *
 * <pre>
 * PropertiesLicenseReader reader = null;
 * try {
 *     reader = new PropertiesLicenseReader(properties, publicKey);
 * } catch (LicenseFormatException e) {
 *     // License format is not valid.
 * }
 *
 * if (reader.isSignatureValid()) {
 *     Map<String, String> features = reader.getLicense().getFeatures();
 *     // do stuff with features
 * }
 * </pre>
 *
 */
public class PropertiesLicenseReader implements LicenseReader {

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
     * Properties keys in order.
     */
    protected List<String> keys;

    /**
     * Only used by subclasses.
     */
    protected PropertiesLicenseReader(final String publicKey) {
        this.publicKey = publicKey;
        this.license = new DefaultLicense();
        this.keys = new ArrayList<String>();
    }

    /**
     * Constructor.
     *
     * @param licenseAsString
     * @param publicKey
     * @throws LicenseFormatException
     */
    public PropertiesLicenseReader(final InputStream license, final String publicKey) throws LicenseFormatException {
        this(license, publicKey, false);
    }

    /**
     * Read a license given as JSON string.
     */
    public PropertiesLicenseReader(final InputStream licenseInputStream, final String publicKey, final boolean isBase64)
            throws LicenseFormatException {
        this.license = new DefaultLicense();
        this.publicKey = publicKey;
        Properties decoded = null;
        if (isBase64) {
            String licenseString;
            try {
                licenseString = IOUtils.toString(licenseInputStream, "ISO-8859-1");
                decoded = read(new ByteArrayInputStream(Base64.decodeBase64(licenseString)));
            } catch (IOException e) {
                throw new LicenseFormatException("Can not load license from properties", e);
            }
        } else {
            try {
                decoded = read(licenseInputStream);
            } catch (IOException e) {
                throw new LicenseFormatException("Can not load license from properties", e);
            }
        }

        for (Object key : decoded.keySet()) {
            String name = (String) key;
            if (name.equals(SIGNATURE_KEY)) {
                signature = decoded.getProperty(name);
            } else {
                license.addFeature(name, decoded.getProperty(name));
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
            sig.update(asPropertiesString(false).getBytes());
            return sig.verify(decodeHex);
        } else {
            return false;
        }
    }

    /**
     * Returns the properties representation of the license including the signature field.
     */
    public String asPropertiesString() {
        return asPropertiesString(true);
    }

    /**
     * Returns the JSON representation of the license including the signature field.
     * If includeSignature is true the signature field will be included.
     */
    protected String asPropertiesString(final boolean includeSignature) {
        Properties properties = new Properties();
        Map<String, String> features = this.license.getFeatures();
        StringBuffer buffer = new StringBuffer();
        for (String name : this.keys) {
            buffer.append(name).append("=").append(features.get(name)).append("\n");
        }
        if (includeSignature) {
            properties.put(SIGNATURE_KEY, this.signature);
            buffer.append(SIGNATURE_KEY).append("=").append(this.signature);
        }
        return buffer.toString();
    }

    /**
     * We have load the properties in this way because the Properties class does not support
     * keys (left side of =) containing spaces.
     */
    private Properties read(final InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        String name;
        String value;
        Properties prop = new Properties();
        this.keys = new ArrayList<String>();
        while ((line = reader.readLine()) != null) {
            if ((line.trim()).indexOf('#') == 0 || (line.trim()).indexOf('!') == 0) {
                continue;
            }
            int index = line.indexOf('=');
            if (index > 0) {
                name = line.substring(0, index).trim();
                value = line.substring(++index).trim();
            } else {
                name = line.trim();
                value = "";
            }
            if (!name.isEmpty()) {
                if (!name.equals(SIGNATURE_KEY)) {
                    this.keys.add(name);
                }
                prop.setProperty(name, value);
            }
        }
        return prop;
    }
}
