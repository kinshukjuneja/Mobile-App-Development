package edu.neu.madcourse.kinshukjuneja.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.activity.horoscope.HoroscopeMainActivity;
import edu.neu.madcourse.kinshukjuneja.activity.horoscope.HoroscopeUserDetailsActivity;
import edu.neu.madcourse.kinshukjuneja.asynctask.DictionaryLoaderTask;
import edu.neu.madcourse.kinshukjuneja.asynctask.horoscope.HoroscopeLoaderTask;
import edu.neu.madcourse.kinshukjuneja.asynctask.SigninTask;
import edu.neu.madcourse.kinshukjuneja.listener.horoscope.HoroscopeReadyListener;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeFirebaseHelper;

public class MainActivity extends AppCompatActivity implements HoroscopeReadyListener {
    private static final String TITLE = "Kinshuk Juneja";

    private HoroscopeFirebaseHelper horoscopeFirebaseHelper;
    private boolean isHoroscopeReady;
    private ProgressBar mainLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        horoscopeFirebaseHelper = HoroscopeFirebaseHelper.getSingletonRef();
        horoscopeFirebaseHelper.attachHoroscopeReadyListener(this);
        startAsyncTasks();
        setTitle(TITLE);
        setContentView(R.layout.activity_main);
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = pInfo.versionName;
            int versionNumber = pInfo.versionCode;

            TextView versionNumberTV = (TextView)findViewById(R.id.versionNumber);
            versionNumberTV.setText("Version Code: " + versionNumber);
            versionNumberTV.setTextColor(Color.WHITE);

            TextView versionNameTV = (TextView)findViewById(R.id.versionName);
            versionNameTV.setText("Version Name: " + versionName);
            versionNameTV.setTextColor(Color.WHITE);
            versionNameTV.setTextColor(Color.WHITE);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mainLoading = (ProgressBar)findViewById(R.id.mainLoading);
        mainLoading.setVisibility(View.GONE);
    }

    private void startAsyncTasks() {
        new HoroscopeLoaderTask().execute();
        new SigninTask().execute();
        new DictionaryLoaderTask().execute();
    }

    public void onClickAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void onClickDictionary(View view) {
        Intent intent = new Intent(this, DictionaryActivity.class);
        startActivity(intent);
    }

    public void onClickWordGame(View view) {
        Intent intent = new Intent(this, ScroggleMainActivity.class);
        startActivity(intent);
    }

    public void onClickHoroscope(View view) {
        if(!isHoroscopeReady) mainLoading.setVisibility(View.VISIBLE);
        else beginHoroscope();

    }

    public void beginHoroscope() {
        if(!horoscopeFirebaseHelper.isUserRegistered()) {
            Intent intent = new Intent(this, HoroscopeUserDetailsActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, HoroscopeMainActivity.class);
            startActivity(intent);
        }
    }

    public void onClickError(View view) {
        int a = 1 / 0;
    }

    @Override
    public void onHoroscopeReady() {
        isHoroscopeReady = true;
        if(mainLoading.getVisibility() == View.VISIBLE) {
            mainLoading.setVisibility(View.GONE);
            beginHoroscope();
        }
    }

}