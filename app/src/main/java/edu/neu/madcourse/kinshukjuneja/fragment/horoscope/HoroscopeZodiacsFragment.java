package edu.neu.madcourse.kinshukjuneja.fragment.horoscope;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.activity.horoscope.HoroscopeMainActivity;

public class HoroscopeZodiacsFragment extends HoroscopeFragment {

    private GridLayout grid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horoscope_zodiacs, container, false);

        grid = (GridLayout)view.findViewById(R.id.fhzGrid);
        attachListeners();

        return view;
    }

    public void attachListeners() {
        for(int i = 0; i < grid.getChildCount(); ++i) {
            final int zodiacId = i;
            ((AppCompatImageButton)grid.getChildAt(i)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HoroscopeMainActivity)getActivity()).zodiacItemClicked(zodiacId);
                }
            });
        }
    }

}
