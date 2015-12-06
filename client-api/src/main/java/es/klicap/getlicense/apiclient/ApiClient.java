/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: ApiClient.java 45 2014-02-15 00:25:11Z recena $
 */
package es.klicap.getlicense.apiclient;

import java.io.File;
import java.net.URL;
import java.util.List;

import es.klicap.getlicense.apiclient.model.Customer;
import es.klicap.getlicense.apiclient.model.KeeValue;
import es.klicap.getlicense.apiclient.model.License;
import es.klicap.getlicense.apiclient.model.LicenseType;
import es.klicap.getlicense.apiclient.model.Product;
import es.klicap.getlicense.apiclient.model.ServerInfo;
import es.klicap.getlicense.apiclient.model.User;

/**
 * TODO: Add description.
 */
public interface ApiClient {

    /**
     * Return a listing of type of licenses.
     *
     * @return
     */
    List<LicenseType> getLicenseTypes();

    /**
     * Return a license type.
     *
     * @param id
     * @return
     */
    LicenseType getLicenseType(Long id);

    /**
     * Return a license.
     *
     * @param id License identification
     * @return
     */
    License getLicense(Long id);

    /**
     * Create a license.
     *
     * @param type
     * @param customer
     * @return
     */
    String createLicense(Long customerId, Long productId, List<KeeValue> properties);

    /**
     * Validate a license from his Base64 representation.
     *
     * @param base64License
     * @return
     */
    Boolean validateLicense(String base64License);

    /**
     * Download a license file from an unique URL.
     * @param url
     * @return
     */
    File download(URL url);

    /**
     * Create a new user.
     *
     * @param username
     * @param email
     * @return
     */
    User createUser(String username, String email);

    /**
     * Delete an user.
     *
     * @param id
     */
    void deleteUser(Long id);

    /**
     *
     * @param id
     * @param username
     * @param email
     */
    void updateUser(Long id, String username, String email);

    /**
     * Retrieve all users.
     *
     * @return
     */
    List<User> getUsers();

    /**
     * Create a new customer.
     *
     * @param name
     * @param owner
     * @return
     */
    Customer createCustomer(String name, Long owner);

    /**
     * Delete a customer.
     *
     * @param id
     */
    void deleteCustomer(Long id);

    /**
     * Retrieve all customers.
     *
     * @return
     */
    List<Customer> getCustomers();

    /**
     * Retrieve information about API Server.
     *
     * @return
     */
    ServerInfo getServerInfo();

    /**
     * Create a new product.
     *
     * @param name
     * @param owner
     * @return
     */
    Product createProduct(String name, Long owner);

    /**
     * Delete a product.
     *
     * @param id
     */
    void deleteProduct(Long id);

    /**
     * Retreive all products.
     *
     * @return
     */
    List<Product> getProducts();
}
