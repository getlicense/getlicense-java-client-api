/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: LicenseWriter.java 90 2014-03-30 19:38:41Z amuniz $
 */
package es.klicap.getlicense.filetoolkit;

import es.klicap.getlicense.filetoolkit.exception.InvalidKeysException;

/**
 * License writer.
 */
public interface LicenseWriter extends LicenseReader {

    /**
     * Add a new feature to the underlying license.
     */
    LicenseWriter addFeature(String feature, String value);

    /**
     * Update the value of a feature in the underlying license.
     */
    LicenseWriter setFeature(String feature, String value);

    /**
     * Delete feature from the underlying license.
     */
    LicenseWriter deleteFeature(String feature);

    /**
     * Returns the underlying license as a string.
     * The format depends on the concrete implementation.
     * 
     * The string returned includes the license signature.
     * 
     * Throws InvalidKeysException if the writer can not generate the sign due to keys problems.
     */
    String write() throws InvalidKeysException;

    /**
     * Returns the underlying license as a string codified in Base64.
     * 
     * The string returned includes the license signature.
     * 
     * Throws InvalidKeysException if the writer can not generate the sign due to keys problems.
     */
    String writeB64() throws InvalidKeysException;

}
