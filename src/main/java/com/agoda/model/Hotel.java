package com.agoda.model;

import com.agoda.Storage;

/**
 * Created by Gaurav on 16/09/17.
 */
public class Hotel extends Storage {

    private long hotelId;

    private String cityName;

    private String roomType;

    private long price;

    public long getHotelId() {
        return hotelId;
    }

    /**
     * @param hotelId
     */

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getCityName() {
        return cityName;
    }

    /**
     * @param cityName
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRoomType() {
        return roomType;
    }

    /**
     * @param roomType
     */
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    /**
     * @return toString
     */
    @Override
    public String toString() {
        return " Hotel Id: " + hotelId
                + " City Name: " + cityName
                + " Room type: " + roomType
                + " Price: " + price + "\n";
    }
}
