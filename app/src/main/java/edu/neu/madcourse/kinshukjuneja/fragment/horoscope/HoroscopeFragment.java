package edu.neu.madcourse.kinshukjuneja.fragment.horoscope;

import android.support.v4.app.Fragment;

import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeFirebaseHelper;

public abstract class HoroscopeFragment extends Fragment {

    protected HoroscopeFirebaseHelper horoscopeFirebaseHelper;

    public HoroscopeFragment() {
        horoscopeFirebaseHelper = HoroscopeFirebaseHelper.getSingletonRef();
    }

}
