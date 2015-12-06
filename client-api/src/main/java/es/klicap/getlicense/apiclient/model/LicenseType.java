/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: LicenseType.java 45 2014-02-15 00:25:11Z recena $
 */
package es.klicap.getlicense.apiclient.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 *
 */
public class LicenseType implements Serializable {

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
    private List<String> attributes = new ArrayList<String>();

    /**
     *
     */
    private Product product;

    /**
     *
     */
    public LicenseType() {
    }

    /**
     *
     * @param json
     */
    public LicenseType(final JsonObject json) {
        super();
        id = json.get("id").asLong();
        name = json.get("name").asString();
        for (JsonValue v : json.get("attributes").asArray()) {
            attributes.add(v.asString());
        }
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
     * @return the attributes
     */
    public List<String> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(final List<String> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(final Product product) {
        this.product = product;
    }

    /**
    *
    * @return
    */
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.add("id", id);
        obj.add("name", name);
        JsonArray attrs = new JsonArray();
        for (String s : attributes) {
            attrs.add(s);
        }
        obj.add("attributes", attrs);
        return obj;
    }

}
