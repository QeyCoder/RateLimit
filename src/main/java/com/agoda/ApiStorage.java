package com.agoda;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by Gaurav on 19/09/17.
 */
public class ApiStorage<T extends Storage> {

    private static final float THRESHOLD = 0.75f;
    private int size;
    private ApiEntry[] apiEntries;
    private int count = -1;

    /**
     * @param size
     */
    public ApiStorage(int size) {
        this.size = size;
        apiEntries = (ApiEntry[]) Array.newInstance(ApiEntry.class, size);
    }

    /**
     * @param key
     * @param val
     */
    public void put(String key, T val) {
        synchronized (ApiStorage.class) {
            ApiEntry apiEntry = new ApiEntry(key, val);
            ApiEntry data = get(key);

            if (count * THRESHOLD >= size) {
                size = size * 2;
                apiEntries = Arrays.copyOf(apiEntries, size);
            }

            if (data == null) {
                count = count + 1;
                apiEntry.setIndex(count);
                try {
                    apiEntries[count] = apiEntry;
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            } else {
                apiEntries[apiEntry.getIndex()] = apiEntry;
            }
        }
    }

    /**
     * @param apiKey
     * @return
     */
    public ApiEntry get(String apiKey) {
        synchronized (ApiEntry.class) {
            for (int i = 0; i < apiEntries.length; i++) {
                ApiEntry entry = apiEntries[i];
                if (entry != null && entry.getKey().equalsIgnoreCase(apiKey)) {
                    return entry;
                }
            }
            return null;
        }
    }

    public ApiEntry[] getApiEntries() {
        return apiEntries;
    }

    int getIndex(String apiKey) {

        //Calculate hash for api key and return index
        //store value at calculated hash index instead of incremental indexes

        return 0;


    }

    public class ApiEntry {
        private String key;
        private T t;
        private int index;

        public ApiEntry(String key, T t) {
            this.key = key;
            this.t = t;
        }


        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }


        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        /**
         * @return hashcode
         */
        @Override
        public int hashCode() {
            //API key is unique
            return System.identityHashCode(key);
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }


}
