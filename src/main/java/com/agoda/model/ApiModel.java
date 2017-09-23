package com.agoda.model;

import com.agoda.Storage;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Gaurav
 *         Created by Gaurav on 17/09/17.
 */
public class ApiModel extends Storage {

    private String apiKey;
    /**
     * limit per 10 sec
     */
    private AtomicInteger count = new AtomicInteger(0);

    private long limit;

    private boolean isBlocked;
    private long lastAccessedTime =-1;


    public ApiModel() {
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public AtomicInteger getCount() {
        return count;
    }


    public String getApiKey() {
        return apiKey;
    }

    /**
     * @param apiKey
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    /**
     * @param lastAccessedTime
     */
    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    /**
     * @param isBlocked
     */
    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public void incrementCount() {
        count.incrementAndGet();

    }

    public void reInitialize() {
        count = new AtomicInteger(1);
    }
}
