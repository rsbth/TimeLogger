package timelogger.mprtcz.com.timelogger.utils.web;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timelogger.mprtcz.com.timelogger.interfaces.Synchrotron;

import static timelogger.mprtcz.com.timelogger.utils.web.WebHandler.handleWebCallbackException;

/**
 * Created by mprtcz on 2017-01-30.
 */
public abstract class CustomWebCallback<T> implements Callback<T> {
    private static final String TAG = "CustomWebCallback";

    public abstract void onSuccessfulCall(Response<T> response);

    public CustomWebCallback() {}

    private Synchrotron synchrotron;
    public CustomWebCallback(Synchrotron synchrotron) {
        this.synchrotron = synchrotron;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            Log.i(TAG, "Call successful, call = " +call);
            onSuccessfulCall(response);
        } else {
            WebHandler.handleBadCodeResponse(call, response);
            this.synchrotron.completeSynchronization();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        handleWebCallbackException(call, throwable);
        this.synchrotron.completeSynchronization();
    }
}