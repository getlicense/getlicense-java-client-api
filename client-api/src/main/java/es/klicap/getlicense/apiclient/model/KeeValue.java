/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: KeeValue.java 45 2014-02-15 00:25:11Z recena $
 */
package es.klicap.getlicense.apiclient.model;

import java.io.Serializable;

import com.eclipsesource.json.JsonObject;

/**
 *
 */
public class KeeValue implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3745716188798053195L;

    /**
     *
     */
    private String kee;

    /**
     *
     */
    private String value;

    /**
     *
     * @param kee
     * @param value
     */
    public KeeValue() {
    }

    /**
     *
     * @param kee
     * @param value
     */
    public KeeValue(final String kee, final String value) {
        super();
        this.kee = kee;
        this.value = value;
    }

    /**
     *
     * @param json
     */
    public KeeValue(final JsonObject json) {
        super();
        kee = json.get("kee").asString();
        value = json.get("value").asString();
    }

    /**
     * @return the kee
     */
    public String getKee() {
        return kee;
    }

    /**
     * @param kee the kee to set
     */
    public void setKee(final String kee) {
        this.kee = kee;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.add("kee", kee);
        obj.add("value", value);
        return obj;
    }

}
