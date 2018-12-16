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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.listener.horoscope.HoroscopeClientFinishListener;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.Friend;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeCache;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeHelper;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeType;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.Zodiac;

public class HoroscopeFriendDetailFragment extends HoroscopeFragment implements View.OnClickListener, HoroscopeClientFinishListener {

    private LinearLayout fhfdTop;
    private TextView fhfdName;
    private TextView fhfdZodiac;
    private TextView fhfdCity;
    private TextView fhfdDob;
    private TextView fhfdCompatibility;
    private TextView fhfdPrediction;
    private TextView fhfdContentTV;
    private ImageView fhfdZodiacIV;
    private AppCompatImageButton fhfdHealthB;
    private AppCompatImageButton fhfdPersonalB;
    private AppCompatImageButton fhfdProfessionB;
    private AppCompatImageButton fhfdEmotionsB;
    private AppCompatImageButton fhfdTravelB;
    private AppCompatImageButton fhfdLuckB;
    private MaterialButton fhfdSimilar;

    private ProgressBar fhfdLoading;

    private HoroscopeType currentHoroscopeType;
    private AppCompatImageButton currentSelectedButton;

    private Friend friend;

    private static final String SIMILAR_TEMPLATE = "%s friends have same horoscope";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horoscope_friend_detail, container, false);
        int friendIndex = getArguments().getInt("friendIndex");
        friend = HoroscopeCache.friends.get(friendIndex);

        fhfdTop = view.findViewById(R.id.fhfdTop);
        fhfdName = view.findViewById(R.id.fhfdName);
        fhfdZodiac = view.findViewById(R.id.fhfdZodiac);
        fhfdCity = view.findViewById(R.id.fhfdCity);
        fhfdDob = view.findViewById(R.id.fhfdDob);
        fhfdCompatibility = view.findViewById(R.id.fhfdCompatibility);
        fhfdPrediction = view.findViewById(R.id.fhfdPrediction);
        fhfdContentTV = view.findViewById(R.id.fhfdContentTV);
        fhfdZodiacIV = view.findViewById(R.id.fhfdZodiacIV);
        fhfdHealthB = view.findViewById(R.id.fhfdHealthB);
        fhfdPersonalB = view.findViewById(R.id.fhfdPersonalB);
        fhfdProfessionB = view.findViewById(R.id.fhfdProfessionB);
        fhfdEmotionsB = view.findViewById(R.id.fhfdEmotionsB);
        fhfdTravelB = view.findViewById(R.id.fhfdTravelB);
        fhfdLuckB = view.findViewById(R.id.fhfdLuckB);
        fhfdSimilar = view.findViewById(R.id.fhfdSimilar);
        fhfdLoading = view.findViewById(R.id.fhfdLoading);

        fhfdLoading.setVisibility(View.GONE);
        fhfdSimilar.setVisibility(View.GONE);
        fhfdTop.setVisibility(View.GONE);
        currentHoroscopeType = HoroscopeType.HEALTH;
        currentSelectedButton = fhfdHealthB;

        checkAndDisplayHoroscope(HoroscopeCache.dailyHealthHoroscope, HoroscopeType.HEALTH);
        attachListenersToButtons();

        return view;
    }

    public void loadHoroscope(HoroscopeType horoscopeType, String horoscope) {
        if(horoscopeType == currentHoroscopeType) {
            fhfdLoading.setVisibility(View.GONE);
            fhfdSimilar.setVisibility(View.VISIBLE);
            fhfdTop.setVisibility(View.VISIBLE);
            fhfdPrediction.setText(horoscopeType.getVerbose() + " Prediction");
            fhfdName.setText(friend.getName());
            fhfdZodiac.setText(friend.getZodiac().toString());
            fhfdZodiacIV.setBackgroundResource(friend.getZodiac().image);
            fhfdCity.setText(friend.getCity());
            fhfdDob.setText(fetchPrettyDate());
            fhfdCompatibility.setText(String.valueOf(friend.getCompatibility()) + "%");
            fhfdContentTV.setText(horoscope);
            List<Friend> similarFriends = HoroscopeCache.friendsByZodiacMap.get(friend.getZodiac());
            fhfdSimilar.setText(String.format(SIMILAR_TEMPLATE, similarFriends.size() == 1 ? "No" : String.valueOf(similarFriends.size() - 1)));
        }
    }

    public String fetchPrettyDate() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(friend.getDob());
            sdf.applyPattern("MMM d, yyyy");
            return sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void attachListenersToButtons() {
        fhfdHealthB.setOnClickListener(this);
        fhfdPersonalB.setOnClickListener(this);
        fhfdProfessionB.setOnClickListener(this);
        fhfdEmotionsB.setOnClickListener(this);
        fhfdTravelB.setOnClickListener(this);
        fhfdLuckB.setOnClickListener(this);
        fhfdSimilar.setOnClickListener(this);
    }

    public void checkAndDisplayHoroscope(Map<Zodiac, String> horoscopeMap, HoroscopeType horoscopeType) {
        if(horoscopeMap.get(friend.getZodiac()) == null) {
            fhfdLoading.setVisibility(View.VISIBLE);
            HoroscopeHelper.getHoroscopeForZodiac(friend.getZodiac(), horoscopeType, this);
        } else {
            fhfdLoading.setVisibility(View.GONE);
            loadHoroscope(horoscopeType, horoscopeMap.get(friend.getZodiac()));
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fhfdHealthB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.HEALTH);
                currentHoroscopeType = HoroscopeType.HEALTH;
                currentSelectedButton = fhfdHealthB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyHealthHoroscope, HoroscopeType.HEALTH);
                break;
            case R.id.fhfdPersonalB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.PERSONAL_LIFE);
                currentHoroscopeType = HoroscopeType.PERSONAL_LIFE;
                currentSelectedButton = fhfdPersonalB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyPersonalLifeHoroscope, HoroscopeType.PERSONAL_LIFE);
                break;
            case R.id.fhfdProfessionB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.PROFESSION);
                currentHoroscopeType = HoroscopeType.PROFESSION;
                currentSelectedButton = fhfdProfessionB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyProfessionHoroscope, HoroscopeType.PROFESSION);
                break;
            case R.id.fhfdEmotionsB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.EMOTIONS);
                currentHoroscopeType = HoroscopeType.EMOTIONS;
                currentSelectedButton = fhfdEmotionsB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyEmotionHoroscope, HoroscopeType.EMOTIONS);
                break;
            case R.id.fhfdTravelB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.TRAVEL);
                currentHoroscopeType = HoroscopeType.TRAVEL;
                currentSelectedButton = fhfdTravelB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyTravelHoroscope, HoroscopeType.TRAVEL);
                break;
            case R.id.fhfdLuckB :
                switchButtonImage((AppCompatImageButton)view, HoroscopeType.LUCK);
                currentHoroscopeType = HoroscopeType.LUCK;
                currentSelectedButton = fhfdLuckB;
                checkAndDisplayHoroscope(HoroscopeCache.dailyLuckHoroscope, HoroscopeType.LUCK);
                break;
            case R.id.fhfdSimilar :
                PopupMenu popup = new PopupMenu(getActivity(), view);
                Menu popupMenu = popup.getMenu();
                if( HoroscopeCache.friendsByZodiacMap.containsKey(friend.getZodiac())) {
                    for(Friend friend : HoroscopeCache.friendsByZodiacMap.get(friend.getZodiac())) {
                        if(!friend.getKey().equals(this.friend.getKey())) popupMenu.add(friend.getName());
                    }
                    popup.show();
                }
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
