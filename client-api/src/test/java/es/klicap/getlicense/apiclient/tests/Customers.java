/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: Customers.java 118 2014-04-13 15:55:18Z recena $
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
import es.klicap.getlicense.apiclient.model.Customer;

/**
 * TODO: Add description.
 */
public class Customers {

    private static final Logger LOGGER = LoggerFactory.getLogger(Customers.class);
    private ApiClientImpl client;

    private List<Customer> customers;

    @Test(alwaysRun = true)
    public void create() {
        client = new ApiClientImpl();
        List<String[]> data = new ArrayList<String[]>();
        try {
            InputStream is = getClass().getResourceAsStream("/customers.csv");
            Reader reader = new InputStreamReader(is);
            CSVReader csv = new CSVReader(reader, ';');
            data = csv.readAll();
            for (String[] row : data) {
                client.createCustomer(row[0], 1L);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assert.assertEquals(data.size(), 100);
    }

    @Test(alwaysRun = true, dependsOnMethods = "create")
    public void getAll() {
        customers = client.getCustomers();
        if (customers.size() == 100) {
            Assert.assertEquals(true, true);
        } else {
            Assert.assertEquals(true, false);
        }
    }

    /**
     * TODO: Add description.
     */
    @Test(alwaysRun = true, dependsOnMethods = "getAll")
    public void deleteAll() {
        for (Customer customer : customers) {
            if (customer.getId() != 1) {
                client.deleteCustomer(customer.getId());
            }
        }
        Assert.assertEquals(client.getCustomers().size(), 0);
    }
}