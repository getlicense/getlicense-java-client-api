/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: Products.java 118 2014-04-13 15:55:18Z recena $
 */
package es.klicap.getlicense.apiclient.tests;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import au.com.bytecode.opencsv.CSVReader;
import es.klicap.getlicense.apiclient.ApiClientImpl;
import es.klicap.getlicense.apiclient.model.Product;

public class Products {

    private static final Logger LOGGER = LoggerFactory.getLogger(Products.class);
    private ApiClientImpl client;

    private List<Product> products;

    @Test(alwaysRun = true)
    public void create() {
        client = new ApiClientImpl();
        List<String[]> data = new ArrayList<String[]>();
        try {
            InputStream is = getClass().getResourceAsStream("/products.csv");
            Reader reader = new InputStreamReader(is);
            CSVReader csv = new CSVReader(reader, ';');
            data = csv.readAll();
            for (String[] row : data) {
                client.createProduct(row[0], 1L);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assert.assertEquals(data.size(), 100);
    }

    @Test(alwaysRun = true, dependsOnMethods = "create")
    public void getAll() {
        products = client.getProducts();
        if (products.size() == 100) {
            Assert.assertEquals(true, true);
        } else {
            Assert.assertEquals(true, false);
        }
    }

    @Test(alwaysRun = true, dependsOnMethods = "getAll")
    public void deleteAll() {
        for (Product product : products) {
            if (product.getId() != 1) {
                client.deleteProduct(product.getId());
            }
        }
        Assert.assertEquals(client.getProducts().size(), 0);
    }
}