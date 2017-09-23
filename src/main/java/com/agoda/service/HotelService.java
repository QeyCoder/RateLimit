package com.agoda.service;

import com.agoda.exception.ApiBlockedException;
import com.agoda.exception.CsvReaderException;
import com.agoda.model.Hotel;

import java.util.List;

/**
 * Created by Gaurav on 16/09/17.
 */
public interface HotelService {
    /**
     * @param apiKey
     * @param cityId
     * @param orderBy
     * @return
     */

    List<Hotel> getHotels(String apiKey, String cityId, String orderBy) throws CsvReaderException, ApiBlockedException;
}
