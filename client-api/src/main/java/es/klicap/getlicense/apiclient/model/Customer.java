/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: Customer.java 45 2014-02-15 00:25:11Z recena $
 */
package es.klicap.getlicense.apiclient.model;

import java.io.Serializable;

import com.eclipsesource.json.JsonObject;

/**
 *
 */
public class Customer implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4572779905418053886L;

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private User owner;

    /**
     *
     */
    public Customer() {
    }

    /**
     *
     * @param json
     */
    public Customer(final JsonObject json) {
        super();
        id = json.get("id").asLong();
        name = json.get("name").asString();
        owner = new User(json.get("owner").asObject());
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
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
     *
     * @return
     */
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        if (id != null) {
            obj.add("id", id);
        }
        if (name != null) {
            obj.add("name", name);
        }
        if (owner != null) {
            obj.add("owner", owner.toJson());
        }
        return obj;
    }

    /**
     * @return the owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(final User owner) {
        this.owner = owner;
    }
}