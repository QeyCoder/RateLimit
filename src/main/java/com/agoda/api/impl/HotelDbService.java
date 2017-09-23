package com.agoda.api.impl;

import com.agoda.ApiStorage;
import com.agoda.exception.CsvReaderException;
import com.agoda.model.ApiModel;
import com.agoda.model.Hotel;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaurav on 17/09/17.
 */
@Component
public class HotelDbService {
    /**
     * hotel model storage
     */
    static ApiStorage<ApiModel> apiModels;
    /**
     * hotel list storage
     */
    private static ApiStorage<Hotel> hotels;
    /**
     * configurable paths
     */
    @Value("${apiKeyStoragePath}")
    private String apiKeyStoragePath;
    @Value("${hotelKeyStoragePath}")
    private String hotelKeyStoragePath;

    /**
     * intialize values when system start
     *
     * @throws CsvReaderException
     */
    @PostConstruct
    void fetchData() throws CsvReaderException {
        getHotels();
        getApiModels();
    }

    /**
     * @param path
     * @return data from csv file  List<String[]>
     * @throws CsvReaderException
     */
    List<String[]> readCsv(String path) throws CsvReaderException {
        FileReader fileReader = null;
        try {
            URL url = this.getClass().getClassLoader().getResource(path);
            fileReader = new FileReader(new File(url.getFile()));
            CSVReader csvReader = new CSVReader(fileReader);
            //Ignoreing
            return csvReader.readAll();

        } catch (IOException e) {
            throw new CsvReaderException("Error while reading CSV file: " + path);

        }


    }

    /**
     * @return list of hotels stored in csv
     */
    public ApiStorage<Hotel> getHotels() throws CsvReaderException {
        if (hotels == null) {

            List<String[]> hotelsdata = readCsv(hotelKeyStoragePath);
            hotels = new ApiStorage<>(hotelsdata.size() - 1);
            //ignoring header starting from 1
            for (int i = 1; i < hotelsdata.size(); i++) {
                String[] csvData = hotelsdata.get(i);
                Hotel hotel = new Hotel();
                hotel.setCityName(csvData[0].toLowerCase());
                hotel.setHotelId(Long.valueOf(csvData[1]));
                hotel.setRoomType(csvData[2]);
                hotel.setPrice(Long.valueOf(csvData[3]));
                hotels.put(String.valueOf(hotel.getHotelId()), hotel);
            }
        }
        return hotels;
    }

    /**
     * @return list of api stored
     * @throws CsvReaderException
     */
    public ApiStorage<ApiModel> getApiModels() throws CsvReaderException {
        if (apiModels == null) {
            List<String[]> apiStorage = readCsv(apiKeyStoragePath);
            //ignoring header starting from 1
            apiModels = new ApiStorage<>(apiStorage.size() - 1);
            for (int i = 1; i < apiStorage.size(); i++) {
                String[] csvData = apiStorage.get(i);
                ApiModel apiModel = new ApiModel();
                apiModel.setApiKey(csvData[0]);
                apiModel.setLimit(Integer.valueOf(csvData[1]));
                apiModels.put(apiModel.getApiKey(), apiModel);
            }
        }
        return apiModels;
    }

    /**
     * query hotels and return filterd result
     *
     * @param cityName
     * @return
     * @throws CsvReaderException
     */
    public List<Hotel> queryHotel(String cityName) throws CsvReaderException {
        List<Hotel> hotelList = new ArrayList<>();
        ApiStorage<Hotel> hotels = getHotels();
        ApiStorage<Hotel>.ApiEntry[] entries = hotels.getApiEntries();

        for (ApiStorage<Hotel>.ApiEntry entry : entries) {
            if (entry != null) {
                Hotel hotel = entry.getT();
                if (hotel != null && hotel.getCityName().equalsIgnoreCase(cityName)) {
                    hotelList.add(hotel);
                }
            }

        }
        return hotelList;
    }

    /**
     * @param key
     * @return
     * @throws CsvReaderException
     */
    public ApiModel findApiModel(String key) throws CsvReaderException {
        ApiStorage<ApiModel> models = getApiModels();
        ApiStorage<ApiModel>.ApiEntry model = models.get(key.toLowerCase());
        if (model != null) {
            return model.getT();
        }
        return null;
    }
}
