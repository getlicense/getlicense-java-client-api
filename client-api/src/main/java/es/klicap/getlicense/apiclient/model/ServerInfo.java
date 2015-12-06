/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: ServerInfo.java 45 2014-02-15 00:25:11Z recena $
 */
package es.klicap.getlicense.apiclient.model;

import java.io.Serializable;

import com.eclipsesource.json.JsonObject;

/**
 *
 */
public class ServerInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3745716188798053195L;

    /**
     *
     */
    private String version;

    /**
     *
     */
    public ServerInfo() {
    }

   /**
     *
     * @param json
     */
    public ServerInfo(final JsonObject json) {
        super();
        version = json.get("version").asString();
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(final String version) {
        this.version = version;
    }
}