/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: User.java 45 2014-02-15 00:25:11Z recena $
 */
package es.klicap.getlicense.apiclient.model;

import java.io.Serializable;

import com.eclipsesource.json.JsonObject;

/**
 *
 */
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3745716188798053195L;

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private String username;

    /**
     *
     */
    private String email;

    /**
     *
     */
    private Long creation;

    /**
     * Logical remove.
     */
    private Long deletion;

    /**
     *
     */
    public User() {
    }

    /**
     *
     * @param json
     */
    public User(final JsonObject json) {
        super();
        id = json.get("id").asLong();
        username = json.get("username").asString();
        email = json.get("email").asString();
        creation = json.get("creation").asLong();
        if (json.get("deletion").isNumber()) {
            deletion = json.get("deletion").asLong();
        }
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * @return the deletion
     */
    public Long getDeletion() {
        return deletion;
    }

    /**
     * @param deletion the deletion to set
     */
    public void setDeletion(final Long deletion) {
        this.deletion = deletion;
    }

    /**
     * @return the creation
     */
    public Long getCreation() {
        return creation;
    }

    /**
     * @param creation the creation to set
     */
    public void setCreation(final Long creation) {
        this.creation = creation;
    }

    /**
    *
    * @return
    */
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        if (id != null) {
            obj.add("id", id);
        }
        if (username != null) {
            obj.add("username", username);
        }
        if (email != null) {
            obj.add("email", email);
        }
        if (creation != null) {
            obj.add("creation", creation);
        }
        if (deletion != null) {
            obj.add("deletion", deletion);
        }
        return obj;
    }
}
