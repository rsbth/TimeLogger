package com.mprtcz.timeloggerdesktop.web.webstatic;

import com.mprtcz.timeloggerdesktop.backend.activity.service.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

/**
 * Created by mprtcz on 2017-01-28.
 */
public class WebHandler {
    private static Logger logger = LoggerFactory.getLogger(ActivityService.class);

    public static  <T> void handleBadCodeResponse(Call<T> call, Response<T> response) {
        logger.warn("Bad response for call " + call.request().toString());
        logger.warn("Response not OK, code = " + response.code());
        try {
            logger.warn("Response not OK response.errorBody(), " + response.errorBody().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void handleWebCallbackException(Call<T> call, Throwable throwable) {
        logger.error("Exception while calling {}, exception = {} " ,call.request(), throwable.toString());
        throwable.printStackTrace();
    }
}
