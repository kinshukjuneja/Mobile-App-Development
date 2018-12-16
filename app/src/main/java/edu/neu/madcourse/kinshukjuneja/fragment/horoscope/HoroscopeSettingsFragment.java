package edu.neu.madcourse.kinshukjuneja.fragment.horoscope;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.activity.horoscope.HoroscopeMainActivity;

public class HoroscopeSettingsFragment extends HoroscopeFragment implements CompoundButton.OnCheckedChangeListener {

    private SwitchCompat fhseNotification;
    private SwitchCompat fhseMusic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horoscope_settings, container, false);

        fhseNotification = view.findViewById(R.id.fhseNotification);
        fhseMusic = view.findViewById(R.id.fhseMusic);

        fhseNotification.setChecked(horoscopeFirebaseHelper.isNotificationEnabled());
        fhseMusic.setChecked(horoscopeFirebaseHelper.isMusicEnabled());

        fhseNotification.setOnCheckedChangeListener(this);
        fhseMusic.setOnCheckedChangeListener(this);

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()) {
            case R.id.fhseNotification :
                horoscopeFirebaseHelper.setNotificationEnabled(isChecked);
                break;
            case R.id.fhseMusic :
                horoscopeFirebaseHelper.setMusicEnabled(isChecked);
                ((HoroscopeMainActivity)getActivity()).adjustMusic();
                break;
        }
    }

}