/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: JsonLicenseRWTest.java 322 2015-02-23 19:10:44Z amuniz $
 */
package es.klicap.getlicense.filetoolkit.test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Hex;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import es.klicap.getlicense.filetoolkit.LicenseReader;
import es.klicap.getlicense.filetoolkit.LicenseWriter;
import es.klicap.getlicense.filetoolkit.exception.InvalidKeysException;
import es.klicap.getlicense.filetoolkit.exception.LicenseFormatException;
import es.klicap.getlicense.filetoolkit.json.JsonLicenseReader;
import es.klicap.getlicense.filetoolkit.json.JsonLicenseWriter;

/**
 * License read/write/validate tests.
 */
public class JsonLicenseRWTest {

    private String publicKey;
    private String privateKey;
    private String licenseText;
    private String licenseBase64Text;

    /**
     * Create public/private key pair before start.
     * @throws InvalidKeysException 
     */
    @BeforeClass
    public void init() throws NoSuchAlgorithmException, InvalidKeysException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        SecureRandom sr = new SecureRandom();
        kpg.initialize(1024, new SecureRandom(sr.generateSeed(8)));
        KeyPair pair = kpg.generateKeyPair();
        publicKey = new String(Hex.encodeHex(pair.getPublic().getEncoded()));
        privateKey = new String(Hex.encodeHex(pair.getPrivate().getEncoded()));
        System.out.println("Generating public/private pair for tests.");
    }

    /**
     * Create license.
     * @throws InvalidKeysException 
     */
    @Test
    public void createLicenseTest() throws InvalidKeysException {
        LicenseWriter writer = new JsonLicenseWriter(publicKey, privateKey);
        writer.addFeature("Users", "5");
        licenseText = writer.write();

        System.out.println("License: ");
        System.out.println("  JSON: " + licenseText);
        licenseBase64Text = writer.writeB64();
        System.out.println("  B64: " + licenseBase64Text);
    }

    /**
     * Read/validate license.
     */
    @Test
    public void readLicenseTest() throws LicenseFormatException {
        System.out.println("Checking license as JSON...");
        LicenseReader reader = new JsonLicenseReader(licenseText, publicKey);

        Assert.assertEquals(reader.getLicense().getFeature("Users"), "5");
        Assert.assertTrue(reader.isSignatureValid());
    }

    /**
     * Read/validate license Base64 encoded.
     */
    @Test
    public void readBase64LicenseTest() throws LicenseFormatException {
        System.out.println("Checking license as Base64...");
        LicenseReader reader = new JsonLicenseReader(licenseBase64Text, publicKey, true);

        Assert.assertEquals(reader.getLicense().getFeature("Users"), "5");
        Assert.assertTrue(reader.isSignatureValid());
    }

    /**
     * Add and remove features and check for existence.
     */
    public void addAndRemoveFeatures() throws InvalidKeysException, LicenseFormatException {
        System.out.println("Checking add/delete features...");
        LicenseWriter writer = new JsonLicenseWriter(publicKey, privateKey);
        String license = writer.addFeature("Users", "30").addFeature("Projects", "2").write();
        LicenseReader reader = new JsonLicenseReader(license, publicKey);
        Assert.assertTrue(reader.getLicense().getFeature("Users").equals("30"));
        Assert.assertTrue(reader.getLicense().getFeature("Projects").equals("2"));
        license = writer.deleteFeature("Users").write();
        reader = new JsonLicenseReader(license, publicKey);
        Assert.assertTrue(reader.getLicense().getFeature("Users") == null);
    }

    /**
     * Modify the license and check that it's invalid.
     */
    @Test
    public void readInvalidLicenseTest() throws LicenseFormatException {
        System.out.println("Checking license after modify it (it's invalid now)... ");
        LicenseReader reader =
                new JsonLicenseReader(licenseText.replace("\"Users\":\"5\"", "\"Users\":\"6\""), publicKey);

        Assert.assertEquals(reader.getLicense().getFeature("Users"), "6");
        Assert.assertFalse(reader.isSignatureValid());
    }

    /**
     * Read license with syntax errors.
     */
    @Test(expectedExceptions = LicenseFormatException.class)
    public void readInvalidFormatLicenseTest() throws LicenseFormatException {
        System.out.println("Checking license with systax errors... ");
        new JsonLicenseReader(licenseText.replace("\"Users\":\"5\"", "\"Users\":6\""), publicKey);
    }

    @Test(expectedExceptions = InvalidKeysException.class)
    public void signWithInvalidKeys() throws InvalidKeysException {
        System.out.println("Checking invalid syntax license...");
        LicenseWriter writer = new JsonLicenseWriter("aaxx", "aaxx");
        writer.addFeature("Sockets", "100").write();
    }
}
