package edu.neu.madcourse.kinshukjuneja.activity.horoscope;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.asynctask.horoscope.HoroscopePeopleLoaderTask;
import edu.neu.madcourse.kinshukjuneja.fragment.horoscope.HoroscopeAboutFragment;
import edu.neu.madcourse.kinshukjuneja.fragment.horoscope.HoroscopeAcknowledgmentFragment;
import edu.neu.madcourse.kinshukjuneja.fragment.horoscope.HoroscopeAddAFriendFragment;
import edu.neu.madcourse.kinshukjuneja.fragment.horoscope.HoroscopeFragment;
import edu.neu.madcourse.kinshukjuneja.fragment.horoscope.HoroscopeFriendDetailFragment;
import edu.neu.madcourse.kinshukjuneja.fragment.horoscope.HoroscopeFriendsFragment;
import edu.neu.madcourse.kinshukjuneja.fragment.horoscope.HoroscopeFutureFragment;
import edu.neu.madcourse.kinshukjuneja.fragment.horoscope.HoroscopeNearMeFragment;
import edu.neu.madcourse.kinshukjuneja.fragment.horoscope.HoroscopeSelfFragment;
import edu.neu.madcourse.kinshukjuneja.fragment.horoscope.HoroscopeSettingsFragment;
import edu.neu.madcourse.kinshukjuneja.fragment.horoscope.HoroscopeUpdateFragment;
import edu.neu.madcourse.kinshukjuneja.fragment.horoscope.HoroscopeZodiacDetailFragment;
import edu.neu.madcourse.kinshukjuneja.fragment.horoscope.HoroscopeZodiacsFragment;
import edu.neu.madcourse.kinshukjuneja.listener.horoscope.HoroscopeClientFinishListener;
import edu.neu.madcourse.kinshukjuneja.listener.horoscope.HoroscopePeopleLoadedListener;
import edu.neu.madcourse.kinshukjuneja.utils.NotificationHelper;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.Friend;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeCache;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeFirebaseHelper;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeNotificationHelper;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.Zodiac;

public class HoroscopeMainActivity extends AppCompatActivity implements HoroscopePeopleLoadedListener {

    private DrawerLayout horoscopeDL;
    private Toolbar horoscopeTB;
    private NavigationView horoscopeNV;
    private ProgressBar hMainLoading;

    private HoroscopeFirebaseHelper horoscopeFirebaseHelper;

    private boolean friendsLoaded;
    private boolean nearMeLoaded;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HoroscopeCache.clearCache();

        horoscopeFirebaseHelper = HoroscopeFirebaseHelper.getSingletonRef();
        horoscopeFirebaseHelper.attachHoroscopePeopleLoadedListener(this);
        horoscopeFirebaseHelper.setSharedPreferences(getPreferences(MODE_PRIVATE));
        horoscopeFirebaseHelper.loadFromSharedPreferences();

        HoroscopeNotificationHelper horoscopeNotificationHelper = new HoroscopeNotificationHelper(this);
        HoroscopeCache.notificationHelper = horoscopeNotificationHelper;

        startAsyncTasks();
        setContentView(R.layout.activity_horoscope_main);

        horoscopeTB = findViewById(R.id.horoscopeTB);
        setSupportActionBar(horoscopeTB);

        horoscopeDL = findViewById(R.id.horoscopeDL);
        setupNavigationViewListener();

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, horoscopeDL, horoscopeTB,
                R.string.drawerOpen, R.string.drawerClose);
        horoscopeDL.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.horoscopeF, new HoroscopeSelfFragment()).commit();
            horoscopeNV.setCheckedItem(R.id.menuHome);
        }

        hMainLoading = (ProgressBar)findViewById(R.id.hMainLoading);
        hMainLoading.setVisibility(View.GONE);

        startBackgroundMusic();
    }

    private void startBackgroundMusic() {
        mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor afd = getAssets().openFd("bgmusic.mp3");
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            adjustMusic();
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    public void adjustMusic() {
        if(horoscopeFirebaseHelper.isMusicEnabled()) mediaPlayer.setVolume(1, 1);
        else mediaPlayer.setVolume(0, 0);
    }

    private void startAsyncTasks() {
        new HoroscopePeopleLoaderTask().execute();
    }

    private void setupNavigationViewListener() {
        horoscopeNV = findViewById(R.id.horoscopeNV);
        horoscopeNV.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.menuHome :
                        getSupportFragmentManager().beginTransaction().replace(R.id.horoscopeF, new HoroscopeSelfFragment()).commit();
                        break;
                    case R.id.menuZodiacs :
                        getSupportFragmentManager().beginTransaction().replace(R.id.horoscopeF, new HoroscopeZodiacsFragment()).commit();
                        break;
                    case R.id.menuFriends :
                        if(!friendsLoaded) hMainLoading.setVisibility(View.VISIBLE);
                        else showFriends();
                        break;
                    case R.id.menuAddFriend :
                        getSupportFragmentManager().beginTransaction().replace(R.id.horoscopeF, new HoroscopeAddAFriendFragment()).commit();
                        break;
                    case R.id.menuNearMe :
                        if(!nearMeLoaded) hMainLoading.setVisibility(View.VISIBLE);
                        else showPeopleNearMe();
                        break;
                    case R.id.menuUpdateProfile :
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isFriend", false);

                        HoroscopeUpdateFragment horoscopeUpdateFragment = new HoroscopeUpdateFragment();
                        horoscopeUpdateFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.horoscopeF, horoscopeUpdateFragment).commit();
                        break;
                    case R.id.menuAck :
                        getSupportFragmentManager().beginTransaction().replace(R.id.horoscopeF, new HoroscopeAcknowledgmentFragment()).commit();
                        break;
                    case R.id.menuV2 :
                        getSupportFragmentManager().beginTransaction().replace(R.id.horoscopeF, new HoroscopeFutureFragment()).commit();
                        break;
                    case R.id.menuSettings :
                        getSupportFragmentManager().beginTransaction().replace(R.id.horoscopeF, new HoroscopeSettingsFragment()).commit();
                        break;
                    case R.id.menuAbout :
                        getSupportFragmentManager().beginTransaction().replace(R.id.horoscopeF, new HoroscopeAboutFragment()).commit();
                        break;
                }
                horoscopeDL.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Close drawer before returning from activity
        if (horoscopeDL.isDrawerOpen(GravityCompat.START)) {
            horoscopeDL.closeDrawer(GravityCompat.START);
        } else {
            HoroscopeFragment horoscopeFragment = (HoroscopeFragment)(getSupportFragmentManager().findFragmentById(R.id.horoscopeF));

            if(horoscopeFragment instanceof HoroscopeFriendDetailFragment) showFriends();
            else if(horoscopeFragment instanceof HoroscopeZodiacDetailFragment) {
                getSupportFragmentManager().beginTransaction().replace(R.id.horoscopeF, new HoroscopeZodiacsFragment()).commit();
            } else if(horoscopeFragment instanceof HoroscopeUpdateFragment && ((HoroscopeUpdateFragment) horoscopeFragment).isFriend()) {
                showFriends();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Exit Horoscope?")
                        .setMessage("Are you sure you want to exit?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                HoroscopeCache.clearCache();
                                HoroscopeMainActivity.super.onBackPressed();
                            }
                        }).create().show();
            }
        }
    }

    @Override
    public void onFriendsLoaded() {
        friendsLoaded = true;
        if(hMainLoading.getVisibility() == View.VISIBLE) {
            hMainLoading.setVisibility(View.GONE);
            showFriends();
        }
    }

    @Override
    public void onPeopleNearMeLoaded() {
        nearMeLoaded = true;
        if(hMainLoading.getVisibility() == View.VISIBLE) {
            hMainLoading.setVisibility(View.GONE);
            showPeopleNearMe();
        }
    }

    public void showFriends() {
        getSupportFragmentManager().beginTransaction().replace(R.id.horoscopeF, new HoroscopeFriendsFragment()).commit();
    }

    public void showPeopleNearMe() {
        getSupportFragmentManager().beginTransaction().replace(R.id.horoscopeF, new HoroscopeNearMeFragment()).commit();
    }

    @Override
    protected void onDestroy() {
        horoscopeFirebaseHelper.removePeopleNearMeListener();
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }

    public void friendItemClicked(int friendIndex) {
        if(friendIndex >= 0 && friendIndex < HoroscopeCache.friends.size()) {
            Bundle bundle = new Bundle();
            bundle.putInt("friendIndex", friendIndex);

            HoroscopeFriendDetailFragment horoscopeFriendDetailFragment = new HoroscopeFriendDetailFragment();
            horoscopeFriendDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.horoscopeF, horoscopeFriendDetailFragment).commit();
        }
    }

    public void zodiacItemClicked(int zodiacId) {
        if(zodiacId >= 0 && zodiacId < Zodiac.values().length) {
            Bundle bundle = new Bundle();
            bundle.putInt("zodiacId", zodiacId);

            HoroscopeZodiacDetailFragment horoscopeZodiacDetailFragment = new HoroscopeZodiacDetailFragment();
            horoscopeZodiacDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.horoscopeF, horoscopeZodiacDetailFragment).commit();
        }
    }

    public void editFriendClicked(int friendIndex) {
        if(friendIndex >= 0 && friendIndex < HoroscopeCache.friends.size()) {
            Bundle bundle = new Bundle();
            bundle.putInt("friendIndex", friendIndex);
            bundle.putBoolean("isFriend", true);

            HoroscopeUpdateFragment horoscopeUpdateFragment = new HoroscopeUpdateFragment();
            horoscopeUpdateFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.horoscopeF, horoscopeUpdateFragment).commit();
        }
    }

    public void deleteFriendClicked(final int friendIndex) {
        if(friendIndex >= 0 && friendIndex < HoroscopeCache.friends.size()) {
            Friend friend = HoroscopeCache.friends.get(friendIndex);
            new AlertDialog.Builder(this)
                    .setTitle("Delete " + friend.getName() + " ?")
                    .setMessage("Are you sure you want to delete?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            horoscopeFirebaseHelper.deleteFriend(friendIndex);
                            showFriends();
                        }
                    }).create().show();
        }
    }

}
