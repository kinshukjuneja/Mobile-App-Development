package edu.neu.madcourse.kinshukjuneja.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.utils.ScroggleFirebaseHelper;

public class ScroggleEndActivity extends AppCompatActivity {
    private static final String TITLE = "Scroggle";

    private TextView scoreTV;
    private TextView wordListTV;
    private Button mainMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroggle_end);
        setTitle(TITLE);

        scoreTV = (TextView)findViewById(R.id.bigScoreTV);
        wordListTV = (TextView)findViewById(R.id.wordlistTV);
        mainMenuButton = (Button)findViewById(R.id.mainMenuButton);

        List<String> wordList = getIntent().getStringArrayListExtra("guessedWords");
        wordListTV.setMovementMethod(new ScrollingMovementMethod());
        int totalScore = getIntent().getIntExtra("totalScore", 0);
        int highestScore = getIntent().getIntExtra("highestScore", 0);
        String highestScoredWord = getIntent().getStringExtra("highestScoredWord");

        scoreTV.setText("SCORE : " + String.valueOf(totalScore));
        for(String word : wordList) wordListTV.append(word + "\n");

        ScroggleFirebaseHelper scroggleFirebaseHelper = ScroggleFirebaseHelper.getSingletonRef();
        scroggleFirebaseHelper.addNewScore(totalScore, highestScore, highestScoredWord);
    }

    public void onClickMainMenu(View view) {
        Intent intent = new Intent(this, ScroggleMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        onClickMainMenu(null);
    }
}