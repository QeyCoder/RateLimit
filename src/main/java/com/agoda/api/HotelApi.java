package com.agoda.api;

import com.agoda.exception.ApiBlockedException;
import com.agoda.exception.CsvReaderException;
import com.agoda.exception.NoRecordFoundException;
import com.agoda.exception.SortingException;
import com.agoda.model.Hotel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Gaurav on 16/09/17.
 */
public interface HotelApi {

    /**
     * @param apiKey
     * @param cityId
     * @param orderBy
     * @return
     * @throws SortingException
     */
    @GET
    @Path("/h/get/{apiKey}/{cityId}/{orderBy:.*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    List<Hotel> getHotels(@PathParam("apiKey") String apiKey, @PathParam("cityId") String cityId, @PathParam("orderBy") String orderBy) throws SortingException, CsvReaderException, ApiBlockedException, NoRecordFoundException;

    /**
     * @return
     */
    @GET
    @Path("/ping")
    @Consumes(MediaType.APPLICATION_JSON)
    String ping();
}
