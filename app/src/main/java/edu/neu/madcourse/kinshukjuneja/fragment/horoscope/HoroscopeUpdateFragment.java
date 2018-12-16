package edu.neu.madcourse.kinshukjuneja.fragment.horoscope;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.Friend;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeCache;

public class HoroscopeUpdateFragment extends HoroscopeFragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener, View.OnFocusChangeListener {

    private TextInputLayout hfuNameC;
    private TextInputLayout hfuCityC;
    private TextInputLayout hfuDobC;
    private TextInputEditText hfuName;
    private TextInputEditText hfuCity;
    private TextInputEditText hfuDob;
    private AppCompatButton hfuConfirm;

    private boolean isFriend;
    private int friendIndex;
    private Friend friend;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horoscope_update, container, false);
        isFriend = getArguments().getBoolean("isFriend");
        if(isFriend) {
            friendIndex = getArguments().getInt("friendIndex");
            friend = HoroscopeCache.friends.get(friendIndex);
        }

        hfuNameC = view.findViewById(R.id.hfuNameC);
        hfuCityC = view.findViewById(R.id.hfuCityC);
        hfuDobC = view.findViewById(R.id.hfuDobC);
        hfuName = view.findViewById(R.id.hfuName);
        hfuCity = view.findViewById(R.id.hfuCity);
        hfuDob = view.findViewById(R.id.hfuDob);
        hfuConfirm = view.findViewById(R.id.hfuConfirm);

        hfuDob.setInputType(InputType.TYPE_NULL);

        if(isFriend) populateFieldsWithFriendDetails();
        else populateFieldsWithDefaults();

        hfuName.setOnClickListener(this);
        hfuName.setOnFocusChangeListener(this);
        hfuCity.setOnClickListener(this);
        hfuCity.setOnFocusChangeListener(this);
        hfuDob.setOnClickListener(this);
        hfuDob.setOnFocusChangeListener(this);
        hfuConfirm.setOnClickListener(this);

        return view;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar cal = new GregorianCalendar(i, i1, i2);
        dateFormat.setCalendar(cal);
        hfuDob.setText(dateFormat.format(cal.getTime()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hfuDob:
                DialogFragment newFragment = new HoroscopeUpdateFragment.DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePickerUpdate");
                break;
            case R.id.hfuName:
                hfuNameC.setError(null);
                break;
            case R.id.hfuCity:
                hfuCityC.setError(null);
                break;
            case R.id.hfuConfirm:
                if (hfuName.length() == 0) hfuNameC.setError("please enter name");
                if (hfuCity.length() == 0) hfuCityC.setError("please enter city");
                if (hfuDob.length() == 0) hfuDobC.setError("Please enter date of birth");
                if (hfuName.length() > 0 && hfuDob.length() > 0 && hfuCity.length() > 0) {
                    String newName = hfuName.getText().toString();
                    String newCity = hfuCity.getText().toString();
                    String newDob = hfuDob.getText().toString();

                    String currentName = isFriend ? friend.getName() : HoroscopeCache.currentUser.getName();
                    String currentCity = isFriend ? friend.getCity() : HoroscopeCache.currentUser.getCity();
                    String currentDob = isFriend ? friend.getDob() : HoroscopeCache.currentUser.getDateOfBirth();

                    if (!currentName.equals(newName) || !currentCity.equals(newCity) || !currentDob.equals(newDob)) {
                        if (isFriend)
                            horoscopeFirebaseHelper.updateFriend(friendIndex, newName, newDob, newCity);
                        else horoscopeFirebaseHelper.updateCurrentUser(newName, newDob, newCity);

                        Toast.makeText(getActivity(), "Information Updated", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "No Update Requested", Toast.LENGTH_LONG).show();
                    }
                    hideKeyboard();
                }
                break;
        }
    }

    public void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch(v.getId()) {
            case R.id.hfuName :
                if(hasFocus) hfuNameC.setError(null);
                break;
            case R.id.hfuCity :
                if(hasFocus) hfuCityC.setError(null);
                break;
            case R.id.hfuDob :
                if(hasFocus) {
                    hfuDobC.setError(null);
                    DialogFragment newFragment = new HoroscopeUpdateFragment.DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePickerUpdate");
                }
                break;
        }
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), (HoroscopeUpdateFragment)getFragmentManager().findFragmentById(R.id.horoscopeF), year, month, day);
        }

    }

    public void populateFieldsWithDefaults() {
        hfuName.setText(HoroscopeCache.currentUser.getName());
        hfuCity.setText(HoroscopeCache.currentUser.getCity());
        hfuDob.setText(HoroscopeCache.currentUser.getDateOfBirth());
    }

    public void populateFieldsWithFriendDetails() {
        hfuName.setText(friend.getName());
        hfuCity.setText(friend.getCity());
        hfuDob.setText(friend.getDob());
    }

    public boolean isFriend() {
        return isFriend;
    }

}
