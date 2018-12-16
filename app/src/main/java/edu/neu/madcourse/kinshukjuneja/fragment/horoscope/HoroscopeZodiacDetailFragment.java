package edu.neu.madcourse.kinshukjuneja.fragment.horoscope;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.listener.horoscope.HoroscopeClientFinishListener;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.Friend;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeCache;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeHelper;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeType;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.Zodiac;

public class HoroscopeZodiacDetailFragment extends HoroscopeFragment implements View.OnClickListener, HoroscopeClientFinishListener {

    private TextView fhzdPrediction;
    private TextView fhzdContentTV;
    private ImageView fhzdZodiacIV;
    private AppCompatImageButton fhzdHealthB;
    private AppCompatImageButton fhzdPersonalB;
    private AppCompatImageButton fhzdProfessionB;
    private AppCompatImageButton fhzdEmotionsB;
    private AppCompatImageButton fhzdTravelB;
    private AppCompatImageButton fhzdLuckB;
    private MaterialButton fhzdSimilar;
    private ProgressBar fhzdLoading;

    private HoroscopeType currentHoroscopeType;
    private AppCompatImageButton currentSelectedButton;

    private Zodiac zodiac;

    private static final String SIMILAR_TEMPLATE = "%s friends have same horoscope";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horoscope_zodiac_detail, container, false);
        zodiac = Zodiac.getById(getArguments().getInt("zodiacId"));

        fhzdPrediction = view.findViewById(R.id.fhzdPrediction);
        fhzdContentTV =  view.findViewById(R.id.fhzdContentTV);
        fhzdZodiacIV = view.findViewById(R.id.fhzdZodiacIV);
        fhzdHealthB = view.findViewById(R.id.fhzdHealthB);
        fhzdPersonalB = view.findViewById(R.id.fhzdPersonalB);
        fhzdProfessionB = view.findViewById(R.id.fhzdProfessionB);
        fhzdEmotionsB = view.findViewById(R.id.fhzdEmotionsB);
        fhzdTravelB = view.findViewById(R.id.fhzdTravelB);
        fhzdLuckB = view.findViewById(R.id.fhzdLuckB);
        fhzdSimilar = view.findViewById(R.id.fhzdSimilar);
        fhzdLoading = view.findViewById(R.id.fhzdLoading);

        fhzdLoading.setVisibility(View.GONE);
        fhzdSimilar.setVisibility(View.GONE);
        currentHoroscopeType = HoroscopeType.HEALTH;
        currentSelectedButton = fhzdHealthB;

        checkAndDisplayHoroscope(HoroscopeCache.dailyHealthHoroscope, HoroscopeType.HEALTH);
        attachListenersToButtons();

        return view;
    }

    public void loadHoroscope(HoroscopeType horoscopeType, String horoscope) {
        if(horoscopeType == currentHoroscopeType) {
            fhzdLoading.setVisibility(View.GONE);
            fhzdSimilar.setVisibility(View.VISIBLE);
            fhzdZodiacIV.setBackgroundResource(zodiac.bigImage);
            fhzdPrediction.setText(horoscopeType.getVerbose() + " Prediction");
            fhzdContentTV.setText(horoscope);
            List<Friend> similarFriends = HoroscopeCache.friendsByZodiacMap.get(zodiac);
            fhzdSimilar.setText(String.format(SIMILAR_TEMPLATE, similarFriends == null ? "No" : String.valueOf(similarFriends.size())));
        }
    }

    public void attachListenersToButtons() {
        fhzdHealthB.setOnClickListener(this);
        fhzdPersonalB.setOnClickListener(this);
        fhzdProfessionB.setOnClickListener(this);
        fhzdEmotionsB.setOnClickListener(this);
        fhzdTravelB.setOnClickListener(this);
        fhzdLuckB.setOnClickListener(this);
        fhzdSimilar.setOnClickListener(this);
    }

    public void checkAndDisplayHoroscope(Map<Zodiac, String> horoscopeMap, HoroscopeType horoscopeType) {
        if(horoscopeMap.get(zodiac) == null) {
            fhzdLoading.setVisibility(View.VISIBLE);
            HoroscopeHelper.getHoroscopeForZodiac(zodiac, horoscopeType, this);
        } else {
            fhzdLoading.setVisibility(View.GONE);
            loadHoroscope(horoscopeType, horoscopeMap.get(zodiac));
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fhzdHealthB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.HEALTH);
                currentHoroscopeType = HoroscopeType.HEALTH;
                currentSelectedButton = fhzdHealthB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyHealthHoroscope, HoroscopeType.HEALTH);
                break;
            case R.id.fhzdPersonalB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.PERSONAL_LIFE);
                currentHoroscopeType = HoroscopeType.PERSONAL_LIFE;
                currentSelectedButton = fhzdPersonalB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyPersonalLifeHoroscope, HoroscopeType.PERSONAL_LIFE);
                break;
            case R.id.fhzdProfessionB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.PROFESSION);
                currentHoroscopeType = HoroscopeType.PROFESSION;
                currentSelectedButton = fhzdProfessionB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyProfessionHoroscope, HoroscopeType.PROFESSION);
                break;
            case R.id.fhzdEmotionsB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.EMOTIONS);
                currentHoroscopeType = HoroscopeType.EMOTIONS;
                currentSelectedButton = fhzdEmotionsB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyEmotionHoroscope, HoroscopeType.EMOTIONS);
                break;
            case R.id.fhzdTravelB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.TRAVEL);
                currentHoroscopeType = HoroscopeType.TRAVEL;
                currentSelectedButton = fhzdTravelB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyTravelHoroscope, HoroscopeType.TRAVEL);
                break;
            case R.id.fhzdLuckB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.LUCK);
                currentHoroscopeType = HoroscopeType.LUCK;
                currentSelectedButton = fhzdLuckB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyLuckHoroscope, HoroscopeType.LUCK);
                break;
            case R.id.fhzdSimilar :
                PopupMenu popup = new PopupMenu(getActivity(), view);
                Menu popupMenu = popup.getMenu();
                if( HoroscopeCache.friendsByZodiacMap.containsKey(zodiac)) {
                    for(Friend friend : HoroscopeCache.friendsByZodiacMap.get(zodiac)) {
                        popupMenu.add(friend.getName());
                    }
                }
                popup.show();
        }
    }

    public void switchButtonImage(AppCompatImageButton enabledButton, HoroscopeType newHoroscopeType) {
        if(!currentSelectedButton.equals(enabledButton)) {
            currentSelectedButton.setImageResource(currentHoroscopeType.getUnselectedImg());
            enabledButton.setImageResource(newHoroscopeType.getSelectedImg());
        }
    }

    @Override
    public void onClientResponse(HoroscopeType horoscopeType, String content) {
        loadHoroscope(horoscopeType, content);
    }

}
