/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: LicenseReader.java 78 2014-03-16 23:21:56Z amuniz $
 */
package es.klicap.getlicense.filetoolkit;

/**
 * License reader.
 */
public interface LicenseReader {

    /**
     * Returns the read license.
     */
    License getLicense();

    /**
     * Check for the validity of the underlying license.
     */
    boolean isSignatureValid();

}
