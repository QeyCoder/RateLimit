package com.agoda;

import com.agoda.api.impl.HotelDbService;
import com.agoda.exception.ApiBlockedException;
import com.agoda.exception.CsvReaderException;
import com.agoda.model.ApiModel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Gaurav on 20/09/17.
 */
@Component
@Aspect
public class RequestHandler {
    @Autowired
    private HotelDbService hotelDbService;

    @Value("${api.defaultlimit}")
    private long defaultLimit;
    @Value("${api.suspenssionTime}")
    private long apiUnblockTime;

    @Value("${api.rateTimeFrame}")
    private long timeFrameLimit;

    /**
     * @param joinPoint
     * @throws CsvReaderException
     * @throws ApiBlockedException
     */
    @Around("@annotation(RateLimiter)")
    public Object processRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();
        String apiKey = (String) args[0];
        ApiModel apiModel = hotelDbService.findApiModel(apiKey);
        if (apiModel == null) {
            apiModel = new ApiModel();
            apiModel.setApiKey(apiKey);
            apiModel.setLimit(defaultLimit);
        }
        apiModel  = updateApi(apiModel);
        hotelDbService.getApiModels().put(apiKey,apiModel);
        if(apiModel.isBlocked()){
            throw new ApiBlockedException("API Key: " + apiModel.getApiKey() + " is blocked");
        }
        return proceed;
    }

    /**
     * 1. if time diff is less than 10 and api count limit  is equal to Threshold count
     * 2. Block api key for 5 min and throw exception
     * <p>
     * 3. else if check already blocked
     * 3.1  no : -> update last access time and increment counter
     * 3.2 yes : -> check can we unblock;
     * no : -> do nothing and throw exception
     * yes: -> reinitialize counter and update last update time
     * <p>
     * <p>
     * 4. else Not blocked: update last access time and increment counter.
     *
     * @param apiModel
     * @throws ApiBlockedException
     */
    private ApiModel updateApi(ApiModel apiModel) throws ApiBlockedException {
        long currentTime = System.currentTimeMillis();
        long lastAccessTime = apiModel.getLastAccessedTime();
        long apiCountLimit = apiModel.getLimit();
        long timeDiff = 0;
        AtomicInteger currentCount = apiModel.getCount();
        boolean isBlocked = apiModel.isBlocked();

        if (lastAccessTime != -1) {
            timeDiff = currentTime - lastAccessTime;
        }
        if (isBlocked) {
            if (timeDiff > apiUnblockTime) {
                apiModel.setIsBlocked(false);
                apiModel.reInitialize();
                apiModel.setLastAccessedTime(currentTime);
            } else {
                throw new ApiBlockedException("API Key: " + apiModel.getApiKey() + " is blocked");
            }
        } else if (timeDiff <= timeFrameLimit && apiCountLimit == currentCount.intValue()) {
            apiModel.setIsBlocked(true);
            apiModel.setLastAccessedTime(currentTime);
        } else if (timeDiff > timeFrameLimit) {
            apiModel.reInitialize();
            apiModel.setLastAccessedTime(currentTime);
        } else {
            apiModel.incrementCount();
            apiModel.setLastAccessedTime(currentTime);
        }
        return apiModel;

    }
}
