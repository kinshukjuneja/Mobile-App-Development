package edu.neu.madcourse.kinshukjuneja.utils.horoscope;


import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cz.msebera.android.httpclient.Header;
import edu.neu.madcourse.kinshukjuneja.client.HoroscopeRestClient;
import edu.neu.madcourse.kinshukjuneja.listener.horoscope.HoroscopeClientFinishListener;

public class HoroscopeHelper {

    public static void getHoroscopeForZodiac(final Zodiac zodiac, final HoroscopeType horoscopeType, final HoroscopeClientFinishListener horoscopeClientFinishListener) {
        HoroscopeRestClient.post(zodiac.toString(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject predictionObj = response.getJSONObject(HoroscopeType.PREDICTION.getJsonKey());
                    cacheClientResponse(zodiac,  predictionObj);

                    horoscopeClientFinishListener.onClientResponse(horoscopeType, predictionObj.getString(horoscopeType.getJsonKey()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }

        });
    }

    private static void cacheClientResponse(Zodiac zodiac, JSONObject predictionObj) throws JSONException {
        HoroscopeCache.dailyHealthHoroscope.put(zodiac, predictionObj.getString(HoroscopeType.HEALTH.getJsonKey()));
        HoroscopeCache.dailyPersonalLifeHoroscope.put(zodiac, predictionObj.getString(HoroscopeType.PERSONAL_LIFE.getJsonKey()));
        HoroscopeCache.dailyProfessionHoroscope.put(zodiac, predictionObj.getString(HoroscopeType.PROFESSION.getJsonKey()));
        HoroscopeCache.dailyEmotionHoroscope.put(zodiac, predictionObj.getString(HoroscopeType.EMOTIONS.getJsonKey()));
        HoroscopeCache.dailyTravelHoroscope.put(zodiac, predictionObj.getString(HoroscopeType.TRAVEL.getJsonKey()));
        HoroscopeCache.dailyLuckHoroscope.put(zodiac, predictionObj.getString(HoroscopeType.LUCK.getJsonKey()));
    }

}
