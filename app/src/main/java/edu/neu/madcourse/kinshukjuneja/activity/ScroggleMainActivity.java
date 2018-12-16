package edu.neu.madcourse.kinshukjuneja.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.asynctask.DictionaryLoaderTask;
import edu.neu.madcourse.kinshukjuneja.asynctask.LeaderboardTask;
import edu.neu.madcourse.kinshukjuneja.asynctask.ScoreboardTask;
import edu.neu.madcourse.kinshukjuneja.asynctask.SigninTask;
import edu.neu.madcourse.kinshukjuneja.asynctask.horoscope.HoroscopeLoaderTask;
import edu.neu.madcourse.kinshukjuneja.utils.ScroggleFirebaseHelper;

public class ScroggleMainActivity extends AppCompatActivity {
    private static final String TITLE = "Scroggle";
    private TextView userNameTV;
    private ScroggleFirebaseHelper scroggleFirebaseHelper;

    private static final String WELCOME_TEMPLATE = "Welcome %s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startAsyncTasks();
        setContentView(R.layout.activity_scroggle_main);
        setTitle(TITLE);
        userNameTV = (TextView)findViewById(R.id.username);
        scroggleFirebaseHelper = ScroggleFirebaseHelper.getSingletonRef();
    }

    private void startAsyncTasks() {
        new LeaderboardTask().execute(this);
        new ScoreboardTask().execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        userNameTV.setText(String.format(WELCOME_TEMPLATE, scroggleFirebaseHelper.getCurrentUsername()));
    }

    public void onClickNewGame(View view) {
        Intent intent = new Intent(this, ScroggleGameActivity.class);
        startActivity(intent);
    }

    public void onClickAck(View view) {
        Intent intent = new Intent(this, ScroggleAcknowledgmentsActivity.class);
        startActivity(intent);
    }

    public void onClickRules(View view) {
        Intent intent = new Intent(this, ScroggleRulesActivity.class);
        startActivity(intent);
    }

    public void onClickScoreboard(View view) {
        Intent intent = new Intent(this, ScroggleScoreboardActivity.class);
        startActivity(intent);
    }

    public void onClickLeaderboard(View view) {
        Intent intent = new Intent(this, ScroggleLeaderboardActivity.class);
        startActivity(intent);
    }

    public void onClickChangeUsername(View view) {
        Intent intent = new Intent(this, ScroggleChangeUsernameActivity.class);
        startActivity(intent);
    }

}
