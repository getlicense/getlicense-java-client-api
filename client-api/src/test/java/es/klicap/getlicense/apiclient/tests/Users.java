/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: Users.java 118 2014-04-13 15:55:18Z recena $
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
import es.klicap.getlicense.apiclient.model.User;

public class Users {

    private static final Logger LOGGER = LoggerFactory.getLogger(Users.class);
    private ApiClientImpl client;

    private List<User> users;

    @Test(alwaysRun = true)
    public void create() {
        client = new ApiClientImpl();
        List<String[]> data = new ArrayList<String[]>();
        try {
            InputStream is = getClass().getResourceAsStream("/users.csv");
            Reader reader = new InputStreamReader(is);
            CSVReader csv = new CSVReader(reader, ';');
            data = csv.readAll();
            for (String[] row : data) {
                client.createUser(row[0], row[1]);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assert.assertEquals(data.size(), 100);
    }

    @Test(alwaysRun = true, dependsOnMethods = "create")
    public void getAll() {
        users = client.getUsers();
        if (users.size() > 100) {
            Assert.assertEquals(true, true);
        } else {
            Assert.assertEquals(true, false);
        }
    }

    @Test(alwaysRun = true, dependsOnMethods = "getAll")
    public void deleteAll() {
        for (User user : users) {
            if (user.getId() != 1) {
                client.deleteUser(user.getId());
            }
        }
        Assert.assertEquals(client.getUsers().size(), 1);
    }
}