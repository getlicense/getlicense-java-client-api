/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: InvalidKeysException.java 123 2014-04-15 07:58:13Z recena $
 */
package es.klicap.getlicense.filetoolkit.exception;

/**
 * Thrown when a provided key is not valid.
 */
public class InvalidKeysException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -8384949852582500728L;

    public InvalidKeysException() {
        super();
    }

    public InvalidKeysException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidKeysException(final String message) {
        super(message);
    }

    public InvalidKeysException(final Throwable cause) {
        super(cause);
    }
}
