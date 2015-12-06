/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: LicenseFormatException.java 123 2014-04-15 07:58:13Z recena $
 */
package es.klicap.getlicense.filetoolkit.exception;

/**
 * Thrown when a license with wrong format is read.
 */
public class LicenseFormatException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -3630452816067055112L;

    public LicenseFormatException() {
        super();
    }

    public LicenseFormatException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public LicenseFormatException(final String message) {
        super(message);
    }

    public LicenseFormatException(final Throwable cause) {
        super(cause);
    }
}
