package edu.neu.madcourse.kinshukjuneja.asynctask.horoscope;

import android.os.AsyncTask;

import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeFirebaseHelper;

public class HoroscopePeopleLoaderTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        HoroscopeFirebaseHelper horoscopeFirebaseHelper = HoroscopeFirebaseHelper.getSingletonRef();
        horoscopeFirebaseHelper.getFriends();
        horoscopeFirebaseHelper.getPeopleNearMe();

        return null;
    }

}