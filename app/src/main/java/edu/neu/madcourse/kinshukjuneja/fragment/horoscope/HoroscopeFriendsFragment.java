package edu.neu.madcourse.kinshukjuneja.fragment.horoscope;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.activity.horoscope.HoroscopeMainActivity;
import edu.neu.madcourse.kinshukjuneja.recyclerview.horoscope.HoroscopePeopleAdapter;

public class HoroscopeFriendsFragment extends HoroscopeFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horoscope_people, container, false);

        TextView fhpTitle = view.findViewById(R.id.fhpTitle);
        fhpTitle.setText("Friends");

        RecyclerView recyclerView = view.findViewById(R.id.fhpRV);
        recyclerView.setAdapter(new HoroscopePeopleAdapter(true, recyclerView, (HoroscopeMainActivity) getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);

        return view;
    }

}
