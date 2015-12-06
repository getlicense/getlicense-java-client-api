/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: DefaultLicense.java 78 2014-03-16 23:21:56Z amuniz $
 */
package es.klicap.getlicense.filetoolkit.impl;

import java.util.HashMap;
import java.util.Map;

import es.klicap.getlicense.filetoolkit.License;

/**
 * Default license implementation.
 */
public class DefaultLicense implements License {

    private Map<String, String> features;

    public DefaultLicense() {
        this.features = new HashMap<String, String>();
    }

    public DefaultLicense(final Map<String, String> features) {
        this.features = features;
    }

    /* (non-Javadoc)
     * @see es.klicap.getlicense.filetoolkit.License#getFeature()
     */
    @Override
    public String getFeature(final String feature) {
        return features.get(feature);
    }

    /* (non-Javadoc)
     * @see es.klicap.getlicense.filetoolkit.License#getFeatures()
     */
    @Override
    public Map<String, String> getFeatures() {
        return features;
    }

    public void addFeature(final String feature, final String value) {
        features.put(feature, value);
    }

    public void deleteFeature(final String feature) {
        features.remove(feature);
    }
}
