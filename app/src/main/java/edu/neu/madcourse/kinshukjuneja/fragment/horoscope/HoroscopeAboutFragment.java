package edu.neu.madcourse.kinshukjuneja.fragment.horoscope;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.Help;

public class HoroscopeAboutFragment extends HoroscopeFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horoscope_about, container, false);

        TextView fhaContentTV = view.findViewById(R.id.fhaContentTV);
        fhaContentTV.setText(Help.about);

        return view;
    }

}
