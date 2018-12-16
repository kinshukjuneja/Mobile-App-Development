package edu.neu.madcourse.kinshukjuneja.listener.horoscope;

import java.util.Map;

import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeType;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.Zodiac;

public interface HoroscopeClientFinishListener {

    void onClientResponse(HoroscopeType horoscopeType, String content);

}
