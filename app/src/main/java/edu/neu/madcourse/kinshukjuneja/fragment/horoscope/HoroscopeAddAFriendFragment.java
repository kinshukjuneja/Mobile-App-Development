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
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.neu.madcourse.kinshukjuneja.R;

public class HoroscopeAddAFriendFragment extends HoroscopeFragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener, View.OnFocusChangeListener {

    private TextInputLayout fhafNameC;
    private TextInputLayout fhafCityC;
    private TextInputLayout fhafDobC;
    private TextInputEditText fhafName;
    private TextInputEditText fhafCity;
    private TextInputEditText fhafDob;
    private AppCompatButton fhafConfirm;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar cal = new GregorianCalendar(i, i1, i2);
        dateFormat.setCalendar(cal);
        fhafDob.setText(dateFormat.format(cal.getTime()));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fhafDob:
                DialogFragment newFragment = new HoroscopeAddAFriendFragment.DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePickerAddFriend");
                break;
            case R.id.fhafName:
                fhafNameC.setError(null);
                break;
            case R.id.fhafCity:
                fhafCityC.setError(null);
                break;
            case R.id.fhafConfirm:
                if(fhafName.length() == 0) fhafNameC.setError("please enter name");
                if(fhafCity.length() == 0) fhafCityC.setError("please enter city");
                if(fhafDob.length() == 0) fhafDobC.setError("please enter date of birth");
                if (fhafName.length() > 0 && fhafDob.length() > 0 && fhafCity.length() > 0) {
                    horoscopeFirebaseHelper.addFriend(fhafName.getText().toString(), fhafDob.getText().toString(), fhafCity.getText().toString());
                    Toast.makeText(getActivity(), "Friend Added", Toast.LENGTH_LONG).show();
                    hideKeyboard();
                    clearAllFields();
                }
                break;
        }
    }

    public void clearAllFields() {
        fhafName.setText("");
        fhafCity.setText("");
        fhafDob.setText("");
        fhafName.clearFocus();
        fhafCity.clearFocus();
        fhafDob.clearFocus();
        fhafNameC.setError(null);
        fhafCityC.setError(null);
        fhafDobC.setError(null);
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
            case R.id.fhafName :
                if(hasFocus) fhafNameC.setError(null);
                break;
            case R.id.fhafCity :
                if(hasFocus) fhafCityC.setError(null);
                break;
            case R.id.fhafDob :
                if(hasFocus) {
                    fhafDobC.setError(null);
                    DialogFragment newFragment = new HoroscopeAddAFriendFragment.DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePickerAddFriend");
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
            return new DatePickerDialog(getActivity(), (HoroscopeAddAFriendFragment)getFragmentManager().findFragmentById(R.id.horoscopeF), year, month, day);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horoscope_addfriend, container, false);

        fhafNameC = view.findViewById(R.id.fhafNameC);
        fhafCityC = view.findViewById(R.id.fhafCityC);
        fhafDobC = view.findViewById(R.id.fhafDobC);
        fhafName = view.findViewById(R.id.fhafName);
        fhafCity = view.findViewById(R.id.fhafCity);
        fhafDob = view.findViewById(R.id.fhafDob);
        fhafConfirm = view.findViewById(R.id.fhafConfirm);

        fhafDob.setInputType(InputType.TYPE_NULL);

        fhafName.setOnClickListener(this);
        fhafName.setOnFocusChangeListener(this);
        fhafCity.setOnClickListener(this);
        fhafCity.setOnFocusChangeListener(this);
        fhafDob.setOnClickListener(this);
        fhafDob.setOnFocusChangeListener(this);
        fhafConfirm.setOnClickListener(this);

        return view;
    }

}
