package com.agoda.service.impl;

import com.agoda.RateLimiter;
import com.agoda.api.impl.HotelDbService;
import com.agoda.exception.ApiBlockedException;
import com.agoda.exception.CsvReaderException;
import com.agoda.model.Hotel;
import com.agoda.service.HotelService;
import com.agoda.util.HotelAscSorter;
import com.agoda.util.HotelDescSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Gaurav on 16/09/17.
 */
@Component
public class HotelServiceImpl implements HotelService {


    @Autowired
    private HotelDbService hotelDbService;

    /**
     * @param apiKey
     * @param cityId
     * @param orderBy
     * @return list of hotels in sorted order
     */
    @Override
    @RateLimiter
    public List<Hotel> getHotels(String apiKey, String cityId, String orderBy) throws CsvReaderException,ApiBlockedException {

        Comparator<Hotel> comparator;
        List<Hotel> list = null;
        try {
            list = hotelDbService.queryHotel(cityId);
            switch (orderBy.toLowerCase()) {

                case "desc":
                    comparator = new HotelDescSorter();
                    break;
                case "asc":
                default:
                    comparator = new HotelAscSorter();
                    break;
            }
            Collections.sort(list, comparator);
        } catch (CsvReaderException e) {
          throw  e;
        }
        return list;
    }


}
