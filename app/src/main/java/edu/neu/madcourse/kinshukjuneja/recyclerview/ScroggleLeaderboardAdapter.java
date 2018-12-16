package edu.neu.madcourse.kinshukjuneja.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.dao.Score;

public class ScroggleLeaderboardAdapter extends RecyclerView.Adapter<ScroggleLeaderboardAdapter.LeaderboardViewHolder> {

    private static final String RANK_TEMPLATE = "%d.";
    private static final String TOTAL_SCORE_TEMPLATE = "Total Score : %s";
    private static final String HIGHEST_WORD_SCORE_TEMPLATE = "Word Score : %s";
    private static final String HIGHEST_SCORED_WORD_TEMPLATE = "Word : %s";

    private List<Score> scores = new ArrayList<>();
    private Map<String, String> usernames = new HashMap<>();
    private Context context;
    private boolean orderedByHighestWord;

    public ScroggleLeaderboardAdapter(Context context, boolean orderedByHighestWord) {
        this.scores = scores;
        this.usernames = usernames;
        this.context = context;
        this.orderedByHighestWord = orderedByHighestWord;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scroggle_leaderboard_item, viewGroup, false);
        ScroggleLeaderboardAdapter.LeaderboardViewHolder scoreboardViewHolder = new ScroggleLeaderboardAdapter.LeaderboardViewHolder(view);
        return scoreboardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder leaderboardViewHolder, int i) {
        String totalScoreRaw = Integer.toString(scores.get(i).getTotalScore());
        String highestScoreRaw = Integer.toString(scores.get(i).getHighestScore());
        String totalScoreVerbose = String.format(TOTAL_SCORE_TEMPLATE, totalScoreRaw);
        String highestScoreVerbose = String.format(HIGHEST_WORD_SCORE_TEMPLATE, highestScoreRaw);

        leaderboardViewHolder.usernameTV.setText(usernames.get(scores.get(i).getUid()));
        leaderboardViewHolder.rankTV.setText(String.format(RANK_TEMPLATE, i + 1));
        leaderboardViewHolder.primaryScoreTV.setText(orderedByHighestWord ? highestScoreRaw : totalScoreRaw);
        leaderboardViewHolder.secondaryScoreTV.setText(orderedByHighestWord ? totalScoreVerbose : highestScoreVerbose);
        leaderboardViewHolder.highestScoredWordTV.setText(String.format(HIGHEST_SCORED_WORD_TEMPLATE, scores.get(i).getHighestScoredWord()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        leaderboardViewHolder.timestampTV.setText(simpleDateFormat.format(scores.get(i).getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return Math.min(scores.size(), 10);
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        TextView usernameTV;
        TextView rankTV;
        TextView primaryScoreTV;
        TextView secondaryScoreTV;
        TextView highestScoredWordTV;
        TextView timestampTV;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTV = itemView.findViewById(R.id.lbUsername);
            rankTV = itemView.findViewById(R.id.lbRank);
            primaryScoreTV = itemView.findViewById(R.id.lbPrimaryScore);
            secondaryScoreTV = itemView.findViewById(R.id.lbSecondaryScore);
            highestScoredWordTV = itemView.findViewById(R.id.lbHighestScoredWord);
            timestampTV = itemView.findViewById(R.id.lbTimestamp);
        }

    }

    public void setOrderedByHighestWord(boolean orderedByHighestWord) {
        this.orderedByHighestWord = orderedByHighestWord;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public void setUsernames(Map<String, String> usernames) {
        this.usernames = usernames;
    }

}
