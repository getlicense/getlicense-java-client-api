/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: License.java 45 2014-02-15 00:25:11Z recena $
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
public class License implements Serializable {

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
    private String uuid;

    /**
     *
     */
    private Long creation;

    /**
     *
     */
    private Product product;

    /**
     *
     */
    private List<KeeValue> properties;

    /**
     *
     */
    private Customer customer;

    /**
     * URL to download the license.
     */
    private String url;

    /**
     *
     */
    public License() {
    }

    /**
     *
     * @param json
     */
    public License(final JsonObject json) {
        id = json.get("id").asLong();
        creation = json.get("creation").asLong();
        uuid = json.get("uuid").asString();
        url = json.get("url").asString();
        customer = new Customer(json.get("customer").asObject());
        product = new Product(json.get("product").asObject());
        properties = new ArrayList<KeeValue>();
        for (JsonValue jv : json.get("properties").asArray()) {
            properties.add(new KeeValue(jv.asObject()));
        }
    }

    /**
     *
     */
    public License(final Long customerId, final Long productId, final List<KeeValue> properties) {
        super();
        this.id = null;
        this.customer = new Customer();
        this.customer.setId(customerId);
        this.product = new Product();
        this.product.setId(productId);
        this.properties = new ArrayList<KeeValue>();
        this.properties = properties;
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
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the properties
     */
    public List<KeeValue> getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(final List<KeeValue> properties) {
        this.properties = properties;
    }

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * Returns a JSON as an string.
     *
     * @return
     */
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        if (id != null) {
            obj.add("id", id);
        }
        if (creation != null) {
            obj.add("creation", creation);
        }
        if (uuid != null) {
            obj.add("uuid", uuid);
        }
        if (url != null) {
            obj.add("url", url);
        }
        obj.add("customer", customer.toJson());
        obj.add("product", product.toJson());
        JsonArray jsonArray = new JsonArray();
        for (KeeValue kv : properties) {
            jsonArray.add(kv.toJson());
        }
        obj.add("properties", jsonArray);
        return obj;
    }
}
