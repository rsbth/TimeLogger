package com.mprtcz.timeloggerdesktop.web.webstatic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mprtcz.timeloggerdesktop.web.webstatic.WebHandler.handleWebCallbackException;

/**
 * Created by mprtcz on 2017-01-30.
 */
public abstract class CustomWebCallback<T> implements Callback<T> {
    private static Logger logger = LoggerFactory.getLogger(CustomWebCallback.class);

    public abstract void onSuccessfulCall(Response<T> response);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            logger.info("Call successful, call = {}", call);
            onSuccessfulCall(response);
        } else {
            WebHandler.handleBadCodeResponse(call, response);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        handleWebCallbackException(call, throwable);
    }
}