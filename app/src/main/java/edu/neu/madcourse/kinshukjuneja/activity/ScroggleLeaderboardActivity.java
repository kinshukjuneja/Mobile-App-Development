package edu.neu.madcourse.kinshukjuneja.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.recyclerview.ScroggleLeaderboardAdapter;
import edu.neu.madcourse.kinshukjuneja.utils.ScroggleFirebaseHelper;

public class ScroggleLeaderboardActivity extends AppCompatActivity {

    private Switch aSwitch;
    private boolean orderedByHighestWord;
    private ScroggleFirebaseHelper scroggleFirebaseHelper;
    private ScroggleLeaderboardAdapter adapter;
    private static final String ORDER_BY_TOTAL_SCORE = "Order By Total Score";
    private static final String ORDER_BY_HIGHEST_WORD = "Order By Highest Word";
    ScroggleLeaderboardAdapter scroggleLeaderboardByTotalScoreAdapter = new ScroggleLeaderboardAdapter(this, false);
    ScroggleLeaderboardAdapter scroggleLeaderboardByHighestScoreAdapter = new ScroggleLeaderboardAdapter(this, true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroggle_leaderboard);
        aSwitch = findViewById(R.id.lbOrderBy);
        aSwitch.setText(ORDER_BY_TOTAL_SCORE);
        scroggleFirebaseHelper = ScroggleFirebaseHelper.getSingletonRef();
        scroggleFirebaseHelper.populateLeaderboard(orderedByHighestWord, orderedByHighestWord ? scroggleLeaderboardByHighestScoreAdapter : scroggleLeaderboardByTotalScoreAdapter, this);
    }

    public void onClickOrderBy(View view) {
        orderedByHighestWord = orderedByHighestWord ? false : true;
        aSwitch.setText(orderedByHighestWord ? ORDER_BY_HIGHEST_WORD : ORDER_BY_TOTAL_SCORE);
        scroggleFirebaseHelper.populateLeaderboard(orderedByHighestWord, orderedByHighestWord ? scroggleLeaderboardByHighestScoreAdapter : scroggleLeaderboardByTotalScoreAdapter, this);
    }

}
