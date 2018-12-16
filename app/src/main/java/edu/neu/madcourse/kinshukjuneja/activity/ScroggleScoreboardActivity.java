package edu.neu.madcourse.kinshukjuneja.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.recyclerview.ScroggleScoreboardAdapter;
import edu.neu.madcourse.kinshukjuneja.utils.ScroggleFirebaseHelper;

public class ScroggleScoreboardActivity extends AppCompatActivity {

    private Switch aSwitch;
    private boolean orderedByHighestWord;
    private ScroggleFirebaseHelper scroggleFirebaseHelper;
    private static final String ORDER_BY_TOTAL_SCORE = "Order By Total Score";
    private static final String ORDER_BY_HIGHEST_WORD = "Order By Highest Word";
    ScroggleScoreboardAdapter scroggleScoreboardByTotalScoreAdapter = new ScroggleScoreboardAdapter(this, false);
    ScroggleScoreboardAdapter scroggleScoreboardByHighestScoreAdapter = new ScroggleScoreboardAdapter(this, true);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroggle_scoreboard);
        aSwitch = findViewById(R.id.sbOrderBy);
        aSwitch.setText(ORDER_BY_TOTAL_SCORE);
        scroggleFirebaseHelper = ScroggleFirebaseHelper.getSingletonRef();
        scroggleFirebaseHelper.populateScoreboard(orderedByHighestWord, orderedByHighestWord ? scroggleScoreboardByHighestScoreAdapter : scroggleScoreboardByTotalScoreAdapter, this);
    }

    public void onClickOrderBy(View view) {
        orderedByHighestWord = orderedByHighestWord ? false : true;
        aSwitch.setText(orderedByHighestWord ? ORDER_BY_HIGHEST_WORD : ORDER_BY_TOTAL_SCORE);
        scroggleFirebaseHelper.populateScoreboard(orderedByHighestWord, orderedByHighestWord ? scroggleScoreboardByHighestScoreAdapter : scroggleScoreboardByTotalScoreAdapter, this);
    }

}
