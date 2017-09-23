package com.agoda.api.impl;

import com.agoda.api.HotelApi;
import com.agoda.exception.ApiBlockedException;
import com.agoda.exception.CsvReaderException;
import com.agoda.exception.NoRecordFoundException;
import com.agoda.exception.SortingException;
import com.agoda.model.Hotel;
import com.agoda.service.HotelService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Gaurav on 16/09/17.
 */
@Component("hotelApi")
public class HotelApiImpl implements HotelApi {


    @Autowired
    private HotelService hotelService;

    /**
     * @param apiKey
     * @param cityId
     * @param orderBy
     * @return list based on user request
     * @throws SortingException
     * @throws CsvReaderException
     * @throws ApiBlockedException
     * @throws NoRecordFoundException
     */


    @Override

    public List<Hotel> getHotels(String apiKey, String cityId, String orderBy) throws NoRecordFoundException, CsvReaderException, ApiBlockedException {
        List<Hotel> hotels = null;

        hotels = hotelService.getHotels(apiKey, cityId, orderBy);

        if (CollectionUtils.isEmpty(hotels)) {
            throw new NoRecordFoundException("No result with query");
        } else {
            return hotels;
        }

    }

    /**
     * @return dummy response
     */
    @Override
    public String ping() {
        return "Hello World";
    }


}
