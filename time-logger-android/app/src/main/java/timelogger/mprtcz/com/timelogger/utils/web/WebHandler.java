package timelogger.mprtcz.com.timelogger.utils.web;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by mprtcz on 2017-01-28.
 */
public class WebHandler {
    private static final String TAG = "WebHandler";

    public static  <T> void handleBadCodeResponse(Call<T> call, Response<T> response) {
        Log.w(TAG, "Bad response for call " + call.request().toString());
        Log.w(TAG, "Response not OK, code = " + response.code());
        try {
            Log.w(TAG, "Response not OK response.errorBody(), " + response.errorBody().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void handleWebCallbackException(Call<T> call, Throwable throwable) {
        Log.e(TAG, "Exception while calling " + call.request() +
                " exception = {} " + throwable.toString());
        throwable.printStackTrace();
    }
}
