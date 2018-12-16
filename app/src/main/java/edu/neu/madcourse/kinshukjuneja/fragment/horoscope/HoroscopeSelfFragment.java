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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class HoroscopeSelfFragment extends HoroscopeFragment implements View.OnClickListener, HoroscopeClientFinishListener {

    private TextView fhsNameTV;
    private TextView fhsZodiacTV;
    private TextView fhsContentTV;
    private ImageView fhsZodiacIV;
    private AppCompatImageButton fhsHealthB;
    private AppCompatImageButton fhsPersonalB;
    private AppCompatImageButton fhsProfessionB;
    private AppCompatImageButton fhsEmotionsB;
    private AppCompatImageButton fhsTravelB;
    private AppCompatImageButton fhsLuckB;
    private MaterialButton fhsSimilar;
    private ProgressBar fhsLoading;

    private HoroscopeType currentHoroscopeType;
    private AppCompatImageButton currentSelectedButton;

    private static final String SIMILAR_TEMPLATE = "%s friends have same horoscope";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horoscope_self, container, false);
        fhsNameTV = view.findViewById(R.id.fhsNameTV);
        fhsZodiacTV = view.findViewById(R.id.fhsZodiacTV);
        fhsContentTV = view.findViewById(R.id.fhsContentTV);
        fhsZodiacIV = view.findViewById(R.id.fhsZodiacIV);
        fhsHealthB = view.findViewById(R.id.fhsHealthB);
        fhsPersonalB = view.findViewById(R.id.fhsPersonalB);
        fhsProfessionB = view.findViewById(R.id.fhsProfessionB);
        fhsEmotionsB = view.findViewById(R.id.fhsEmotionsB);
        fhsTravelB = view.findViewById(R.id.fhsTravelB);
        fhsLuckB = view.findViewById(R.id.fhsLuckB);
        fhsSimilar = view.findViewById(R.id.fhsSimilar);
        fhsLoading = view.findViewById(R.id.fhsLoading);

        fhsLoading.setVisibility(View.GONE);
        fhsSimilar.setVisibility(View.GONE);
        currentHoroscopeType = HoroscopeType.HEALTH;
        currentSelectedButton = fhsHealthB;

        checkAndDisplayHoroscope(HoroscopeCache.dailyHealthHoroscope, HoroscopeType.HEALTH);
        attachListenersToButtons();

        return view;
    }

    public void loadHoroscope(HoroscopeType horoscopeType, String horoscope) {
        if(horoscopeType == currentHoroscopeType) {
            fhsLoading.setVisibility(View.GONE);
            fhsSimilar.setVisibility(View.VISIBLE);
            fhsNameTV.setText(horoscopeType.getVerbose() + " Prediction");
            fhsZodiacTV.setText(HoroscopeCache.zodiac.toString());
            fhsZodiacIV.setBackgroundResource(HoroscopeCache.zodiac.image);
            fhsContentTV.setText(horoscope);
            List<Friend> similarFriends = HoroscopeCache.friendsByZodiacMap.get(HoroscopeCache.zodiac);
            fhsSimilar.setText(String.format(SIMILAR_TEMPLATE, similarFriends == null ? "No" : String.valueOf(similarFriends.size())));
        }
    }

    public void attachListenersToButtons() {
        fhsHealthB.setOnClickListener(this);
        fhsPersonalB.setOnClickListener(this);
        fhsProfessionB.setOnClickListener(this);
        fhsEmotionsB.setOnClickListener(this);
        fhsTravelB.setOnClickListener(this);
        fhsLuckB.setOnClickListener(this);
        fhsSimilar.setOnClickListener(this);
    }

    public void checkAndDisplayHoroscope(Map<Zodiac, String> horoscopeMap, HoroscopeType horoscopeType) {
        if(horoscopeMap.get(HoroscopeCache.zodiac) == null) {
            fhsLoading.setVisibility(View.VISIBLE);
            HoroscopeHelper.getHoroscopeForZodiac(HoroscopeCache.zodiac, horoscopeType, this);
        } else {
            fhsLoading.setVisibility(View.GONE);
            loadHoroscope(horoscopeType, horoscopeMap.get(HoroscopeCache.zodiac));
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fhsHealthB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.HEALTH);
                currentHoroscopeType = HoroscopeType.HEALTH;
                currentSelectedButton = fhsHealthB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyHealthHoroscope, HoroscopeType.HEALTH);
                break;
            case R.id.fhsPersonalB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.PERSONAL_LIFE);
                currentHoroscopeType = HoroscopeType.PERSONAL_LIFE;
                currentSelectedButton = fhsPersonalB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyPersonalLifeHoroscope, HoroscopeType.PERSONAL_LIFE);
                break;
            case R.id.fhsProfessionB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.PROFESSION);
                currentHoroscopeType = HoroscopeType.PROFESSION;
                currentSelectedButton = fhsProfessionB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyProfessionHoroscope, HoroscopeType.PROFESSION);
                break;
            case R.id.fhsEmotionsB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.EMOTIONS);
                currentHoroscopeType = HoroscopeType.EMOTIONS;
                currentSelectedButton = fhsEmotionsB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyEmotionHoroscope, HoroscopeType.EMOTIONS);
                break;
            case R.id.fhsTravelB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.TRAVEL);
                currentHoroscopeType = HoroscopeType.TRAVEL;
                currentSelectedButton = fhsTravelB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyTravelHoroscope, HoroscopeType.TRAVEL);
                break;
            case R.id.fhsLuckB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.LUCK);
                currentHoroscopeType = HoroscopeType.LUCK;
                currentSelectedButton = fhsLuckB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyLuckHoroscope, HoroscopeType.LUCK);
                break;
            case R.id.fhsSimilar :
                PopupMenu popup = new PopupMenu(getActivity(), view);
                Menu popupMenu = popup.getMenu();
                if( HoroscopeCache.friendsByZodiacMap.containsKey(HoroscopeCache.zodiac)) {
                    for(Friend friend : HoroscopeCache.friendsByZodiacMap.get(HoroscopeCache.zodiac)) {
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