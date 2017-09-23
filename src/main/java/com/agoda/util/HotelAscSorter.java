package com.agoda.util;

import com.agoda.model.Hotel;

import java.util.Comparator;

/**
 * Created by Gaurav on 17/09/17.
 */
public class HotelAscSorter implements Comparator<Hotel> {

    /**
     * @param hotel1
     * @param hotel2
     * @return
     */
    @Override
    public int compare(Hotel hotel1, Hotel hotel2) {

        long priceDiff = hotel1.getPrice() - hotel2.getPrice();
        return priceDiff >= 0 ? 1 : -1;
    }
}
