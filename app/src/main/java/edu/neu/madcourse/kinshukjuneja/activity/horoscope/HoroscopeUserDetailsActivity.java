package edu.neu.madcourse.kinshukjuneja.activity.horoscope;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeFirebaseHelper;
import edu.neu.madcourse.kinshukjuneja.utils.LocationHelper;

public class HoroscopeUserDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener, View.OnFocusChangeListener {

    private TextInputLayout nameTVC;
    private TextInputLayout cityTVC;
    private TextInputLayout dateTVC;
    private TextInputEditText nameTV;
    private TextInputEditText cityTV;
    private TextInputEditText dateTV;
    private AppCompatButton confirmB;

    private HoroscopeFirebaseHelper horoscopeFirebaseHelper;
    private LocationHelper locationHelper;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.dateTV:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.nameTV:
                nameTVC.setError(null);
                break;
            case R.id.cityTV:
                cityTVC.setError(null);
                break;
            case R.id.confirmB:
                if(nameTV.length() == 0) nameTVC.setError("please enter name");
                if(cityTV.length() == 0) cityTVC.setError("please enter city");
                if(dateTV.length() == 0) dateTVC.setError("please enter date of birth");
                if (nameTV.length() > 0 && dateTV.length() > 0 && cityTV.length() > 0) {
                    horoscopeFirebaseHelper.createNewUser(nameTV.getText().toString(), dateTV.getText().toString(), cityTV.getText().toString());
                    Intent intent = new Intent(this, HoroscopeMainActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch(v.getId()) {
            case R.id.nameTV :
                if(hasFocus) nameTVC.setError(null);
                break;
            case R.id.cityTV :
                if(hasFocus) cityTVC.setError(null);
                break;
            case R.id.dateTV :
                if(hasFocus) {
                    dateTVC.setError(null);
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "datePicker");
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
            return new DatePickerDialog(getActivity(), (HoroscopeUserDetailsActivity)getActivity(), year, month, day);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horoscope_user_details);

        nameTVC = findViewById(R.id.nameTVC);
        cityTVC = findViewById(R.id.cityTVC);
        dateTVC = findViewById(R.id.cityTVC);
        nameTV = findViewById(R.id.nameTV);
        cityTV = findViewById(R.id.cityTV);
        dateTV = findViewById(R.id.dateTV);
        confirmB = findViewById(R.id.confirmB);
        horoscopeFirebaseHelper = HoroscopeFirebaseHelper.getSingletonRef();
        locationHelper = LocationHelper.getSingletonRef(this);
        locationHelper.setupLocationManager(this);

        dateTV.setInputType(InputType.TYPE_NULL);

        nameTV.setOnClickListener(this);
        nameTV.setOnFocusChangeListener(this);
        cityTV.setOnClickListener(this);
        cityTV.setOnFocusChangeListener(this);
        dateTV.setOnClickListener(this);
        dateTV.setOnFocusChangeListener(this);
        confirmB.setOnClickListener(this);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar cal = new GregorianCalendar(i, i1, i2);
        dateFormat.setCalendar(cal);
        dateTV.setText(dateFormat.format(cal.getTime()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationHelper.permissionsGranted(permissions, grantResults);
    }

    public void setCity(String city) {
        cityTV.setText(city);
    }

}
