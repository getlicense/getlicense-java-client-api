/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: License.java 78 2014-03-16 23:21:56Z amuniz $
 */
package es.klicap.getlicense.filetoolkit;

import java.util.Map;

/**
 * License file.
 */
public interface License {

    /**
     * Returns the feature in the license.
     */
    String getFeature(String feature);

    /**
     * Returns all features in the license.
     */
    Map<String, String> getFeatures();

}
