/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: ApiClientImpl.java 118 2014-04-13 15:55:18Z recena $
 */
package es.klicap.getlicense.apiclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import es.klicap.commons.httpwrapper.HttpClient4Connector;
import es.klicap.commons.httpwrapper.Query;
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
public class ApiClientImpl implements ApiClient {


    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiClientImpl.class);

    /**
     *
     */
    private String apiKey;

    /**
     * Configuration properties.
     */
    private Properties config;

    /**
     * API base url.
     */
    private URL apiUrl;

    /**
     *
     */
    private HttpClient4Connector http;

    /**
     *
     * @param apiToken
     */
    public ApiClientImpl(final String yourApiKey) {
        super();
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.xml");
            config = new Properties();
            config.loadFromXML(in);
            apiUrl = new URL(config.getProperty("API_URL"));
            apiKey = yourApiKey;
            LOGGER.info("GL Client API initialized");
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        http = new HttpClient4Connector();
    }

    /**
    *
    * @param apiToken
    */
   public ApiClientImpl() {
       super();
       try {
           InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.xml");
           config = new Properties();
           config.loadFromXML(in);
           apiUrl = new URL(config.getProperty("API_URL"));
           apiKey = config.getProperty("API_KEY");
           LOGGER.info("GL Client API initialized");
       } catch (InvalidPropertiesFormatException e) {
           e.printStackTrace();
       } catch (MalformedURLException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
       http = new HttpClient4Connector();
   }

    @Override
    public List<LicenseType> getLicenseTypes() {
        Query query = new Query(apiUrl.toString(), "/license/type");
        query.addParameter("GL_API_KEY", this.getApiKey());
        HttpResponse response = http.executeGet(query);
        InputStream content;
        List<LicenseType> licenseTypes = new ArrayList<LicenseType>();
        try {
            content = response.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
            String jsonString = writer.toString();
            JsonArray json = JsonArray.readFrom(jsonString);
            for (JsonValue jv : json.asArray()) {
                LicenseType lt = new LicenseType(jv.asObject());
                licenseTypes.add(lt);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return licenseTypes;
    }

    @Override
    public LicenseType getLicenseType(final Long id) {
        LicenseType lt = null;
        Query query = new Query(apiUrl.toString(), "/license/type/" + id);
        query.addParameter("GL_API_KEY", this.getApiKey());
        HttpResponse response = http.executeGet(query);
        InputStream content;
        try {
            content = response.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
            String jsonString = writer.toString();
            JsonArray json = JsonArray.readFrom(jsonString);
            lt = new LicenseType(json.get(0).asObject());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return lt;
    }

    @Override
    public License getLicense(final Long id) {
        License license = null;
        Query query = new Query(apiUrl.toString(), "/license/" + id);
        query.addParameter("GL_API_KEY", this.getApiKey());
        HttpResponse response = http.executeGet(query);
        InputStream content;
        try {
            content = response.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
            String jsonString = writer.toString();
            JsonArray json = JsonArray.readFrom(jsonString);
            license = new License(json.get(0).asObject());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return license;
    }

    @Override
    public File download(final URL url) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean validateLicense(final String base64License) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String createLicense(final Long customerId, final Long productId, final List<KeeValue> properties) {
        String urlToDownloadLicense = null;
        License license = new License(customerId, productId, properties);
        Query query = new Query(apiUrl.toString(), "/license");
        query.addParameter("GL_API_KEY", this.getApiKey());
        query.setBody(license.toJson().toString());
        HttpResponse response = http.executePost(query);
        InputStream content;
        try {
            content = response.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
            License created = new License(JsonObject.readFrom(writer.toString()));
            urlToDownloadLicense = created.getUrl();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return urlToDownloadLicense;
    }

    /**
     * @return the apiKey
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * @param apiKey the apiKey to set
     */
    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @return the config
     */
    public Properties getConfig() {
        return config;
    }

    /**
     * @param config the config to set
     */
    public void setConfig(final Properties config) {
        this.config = config;
    }

    @Override
    public User createUser(final String username, final String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        Query query = new Query(apiUrl.toString(), "/user");
        query.addParameter("GL_API_KEY", this.getApiKey());
        query.setBody(user.toJson().toString());
        HttpResponse response = http.executePost(query);
        InputStream content;
        try {
            content = response.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void deleteUser(final Long id) {
        Query query = new Query(apiUrl.toString(), "/user/" + id);
        query.addParameter("GL_API_KEY", this.getApiKey());
        HttpResponse response = http.executeDelete(query);
        InputStream content;
        try {
            content = response.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(final Long id, final String username, final String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        Query query = new Query(apiUrl.toString(), "/user/" + id);
        query.addParameter("GL_API_KEY", this.getApiKey());
        query.setBody(user.toJson().toString());
        HttpResponse response = http.executePut(query);
        InputStream content;
        try {
            content = response.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getUsers() {
        Query query = new Query(apiUrl.toString(), "/user");
        query.addParameter("GL_API_KEY", this.getApiKey());
        HttpResponse response = http.executeGet(query);
        InputStream content;
        List<User> users = new ArrayList<User>();
        try {
            content = response.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
            JsonArray json = JsonArray.readFrom(writer.toString());
            for (JsonValue jv : json.asArray()) {
                User u = new User(jv.asObject());
                users.add(u);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public ServerInfo getServerInfo() {
        Query query = new Query(apiUrl.toString(), "/server");
        HttpResponse response = http.executeGet(query);
        InputStream content;
        ServerInfo server = new ServerInfo();
        try {
            content = response.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
            server = new ServerInfo(JsonObject.readFrom(writer.toString()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return server;
    }

    @Override
    public Customer createCustomer(final String name, final Long owner) {
        Customer customer = new Customer();
        customer.setName(name);
        User user = new User();
        user.setId(owner);
        customer.setOwner(user);
        Query query = new Query(apiUrl.toString(), "/customer");
        query.addParameter("GL_API_KEY", this.getApiKey());
        query.setBody(customer.toJson().toString());
        HttpResponse response = http.executePost(query);
        InputStream content;
        try {
            content = response.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return customer;
    }

    @Override
    public void deleteCustomer(final Long id) {
        Query query = new Query(apiUrl.toString(), "/customer/" + id);
        query.addParameter("GL_API_KEY", this.getApiKey());
        HttpResponse response = http.executeDelete(query);
        InputStream content;
        try {
            content = response.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> getCustomers() {
        Query query = new Query(apiUrl.toString(), "/customer");
        query.addParameter("GL_API_KEY", this.getApiKey());
        HttpResponse response = http.executeGet(query);
        InputStream content;
        List<Customer> customers = new ArrayList<Customer>();
        try {
            content = response.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
            JsonArray json = JsonArray.readFrom(writer.toString());
            for (JsonValue jv : json.asArray()) {
                Customer u = new Customer(jv.asObject());
                customers.add(u);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public Product createProduct(final String name, final Long owner) {
        Product product = new Product();
        product.setName(name);
        User user = new User();
        user.setId(owner);
        product.setOwner(user);
        Query query = new Query(apiUrl.toString(), "/product");
        query.addParameter("GL_API_KEY", this.getApiKey());
        query.setBody(product.toJson().toString());
        HttpResponse response = http.executePost(query);
        InputStream content;
        try {
            content = response.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return product;

    }

    @Override
    public void deleteProduct(final Long id) {
        Query query = new Query(apiUrl.toString(), "/product/" + id);
        query.addParameter("GL_API_KEY", this.getApiKey());
        HttpResponse response = http.executeDelete(query);
        InputStream content;
        try {
            content = response.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getProducts() {
        Query query = new Query(apiUrl.toString(), "/product");
        query.addParameter("GL_API_KEY", this.getApiKey());
        HttpResponse response = http.executeGet(query);
        InputStream content;
        List<Product> products = new ArrayList<Product>();
        try {
            content = response.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
            JsonArray json = JsonArray.readFrom(writer.toString());
            for (JsonValue jv : json.asArray()) {
                Product u = new Product(jv.asObject());
                products.add(u);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return products;
    }
}