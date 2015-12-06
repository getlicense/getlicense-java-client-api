/*
 * getlicense.io
 * Copyright (C) 2015 klicap - ingeniería del puzle
 *
 * $Id: StringKeyPair.java 351 2015-03-14 10:45:21Z amuniz $
 */
package es.klicap.getlicense.filetoolkit.key;

public class StringKeyPair {

    private String privateKey;

    private String publicKey;
    
    public StringKeyPair(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

}
