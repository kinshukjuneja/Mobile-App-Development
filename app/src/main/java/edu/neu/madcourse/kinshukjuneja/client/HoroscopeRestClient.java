package edu.neu.madcourse.kinshukjuneja.client;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HoroscopeRestClient {

    private static final String BASE_URL = "https://json.astrologyapi.com/v1/sun_sign_prediction/daily/";
    private static final String username = "603275";
    private static final String password = "e1a55d5db91c0ebeda2b043bdd20ffb1";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void post(String relativeUrl, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setBasicAuth(username, password);
        client.post(getAbsoluteUrl(relativeUrl), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
