package com.agoda.test;

import com.agoda.model.Hotel;
import com.agoda.util.HotelDescSorter;
import com.google.gson.Gson;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Gaurav on 23/09/17.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RateLimitTest {


    /**
     * test case for ping url
     */

    @Test
    public void ping() {
        Response response = getResponse("ping");
        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("Hello World", response.readEntity(String.class));
    }

    /**
     *
     */
    @Test
    public void getHotelsWhenKeyExist() {

        Response response = getResponse("h/get/ABC/Ashburn/");

        Assert.assertEquals(200, response.getStatus());
        Hotel[] parsedData = parseJsonArray(response);
        Assert.assertNotNull(parsedData);
    }

    /**
     *
     */
    @Test
    public void isDataSorted() {
        Response response = getResponse("h/get/ABC/Ashburn/DESC");
        Assert.assertEquals(200, response.getStatus());
        Hotel[] parsedData = parseJsonArray(response);
        Assert.assertNotNull(parsedData);
        List<Hotel> list = Arrays.asList(parsedData);

        List<Hotel> newlist = Arrays.asList(Arrays.copyOf(parsedData, parsedData.length));
        newlist.sort(new HotelDescSorter());
        Assert.assertEquals(list, newlist);


    }

    /**
     *
     */
    @Test
    public void getHotelsWhenKeyNotExist() {

        Response response = getResponse("h/get/TEST/Ashburn/DESC");
        Assert.assertEquals(200, response.getStatus());
        Hotel[] parsedData = parseJsonArray(response);
        Assert.assertNotNull(parsedData);

    }

    /**
     *
     */
    @Test
    public void getUnknownCityRecord() {

        Response response = getResponse("h/get/ABC/DELHI/DESC");
        Assert.assertEquals(500, response.getStatus());

    }

    /**
     *
     */
    @Test
    public void getDataAndBlockKey() {

        WebTarget config = getClientConfig();
        //CDE LIMIT is 5 and as we are hiting url continuous so request will complete within  10 sec

        for (int i = 0; i < 5; i++) {
            Response response = getResponse("h/get/CDE/Ashburn/DESC");
            Assert.assertEquals(200, response.getStatus());
            Hotel[] parsedData = parseJsonArray(response);
            Assert.assertNotNull(parsedData);
        }
        Response response = getResponse("h/get/CDE/Ashburn/DESC");
        Assert.assertEquals(500, response.getStatus());

    }

    /**
     *
     */
     @Test
    public void zblockKeyAndGetDataAfter5Minutes() throws InterruptedException {
        WebTarget config = getClientConfig();
        //DEF LIMIT is 7 and as we are hiting url continuous so request will complete within  10 sec

        for (int i = 0; i < 7; i++) {
            Response response = getResponse("h/get/DEF/Ashburn/DESC");
            Assert.assertEquals(200, response.getStatus());
            Hotel[] parsedData = parseJsonArray(response);
            Assert.assertNotNull(parsedData);
        }
        Thread.sleep(300000);
        Response response = getResponse("h/get/DEF/Ashburn/DESC");
        Assert.assertEquals(200, response.getStatus());


    }

    /**
     *
     * @param url
     * @return
     */
    Response getResponse(String url) {
        return getClientConfig().path(url).
                request().
                accept(MediaType.APPLICATION_JSON).
                get();
    }

    /**
     *
     * @param response
     * @return
     */
    Hotel[] parseJsonArray(Response response) {
        return new Gson().fromJson(response.readEntity(String.class), Hotel[].class);
    }

    /**
     * clilent configuration
     *
     * @return WebTarget
     */
    private WebTarget getClientConfig() {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(getBaseURI());
        return target;
    }

    /**
     * Base URL of application
     *
     * @return
     */
    private String getBaseURI() {
        return "http://localhost:8085/api";
    }

}
