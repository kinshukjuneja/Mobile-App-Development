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
import java.util.List;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.dao.Score;

public class ScroggleScoreboardAdapter extends RecyclerView.Adapter<ScroggleScoreboardAdapter.ScoreboardViewHolder> {

    private static final String RANK_TEMPLATE = "%d.";
    private static final String TOTAL_SCORE_TEMPLATE = "Total Score : %s";
    private static final String HIGHEST_WORD_SCORE_TEMPLATE = "Word Score : %s";
    private static final String HIGHEST_SCORED_WORD_TEMPLATE = "Word : %s";

    private List<Score> scores = new ArrayList<>();
    private Context context;
    private boolean orderedByHighestWord;

    public ScroggleScoreboardAdapter(Context context, boolean orderedByHighestWord) {
        this.context = context;
        this.orderedByHighestWord = orderedByHighestWord;
    }

    @NonNull
    @Override
    public ScoreboardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scroggle_scoreboard_item, viewGroup, false);
        ScoreboardViewHolder scoreboardViewHolder = new ScoreboardViewHolder(view);
        return scoreboardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreboardViewHolder scoreboardViewHolder, int i) {
        String totalScoreRaw = Integer.toString(scores.get(i).getTotalScore());
        String highestScoreRaw = Integer.toString(scores.get(i).getHighestScore());
        String totalScoreVerbose = String.format(TOTAL_SCORE_TEMPLATE, totalScoreRaw);
        String highestScoreVerbose = String.format(HIGHEST_WORD_SCORE_TEMPLATE, highestScoreRaw);

        scoreboardViewHolder.rankTV.setText(String.format(RANK_TEMPLATE, i + 1));
        scoreboardViewHolder.primaryScoreTV.setText(orderedByHighestWord ? highestScoreRaw : totalScoreRaw);
        scoreboardViewHolder.secondaryScoreTV.setText(orderedByHighestWord ? totalScoreVerbose : highestScoreVerbose);
        scoreboardViewHolder.highestScoredWordTV.setText(String.format(HIGHEST_SCORED_WORD_TEMPLATE, scores.get(i).getHighestScoredWord()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        scoreboardViewHolder.timestampTV.setText(simpleDateFormat.format(scores.get(i).getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public class ScoreboardViewHolder extends RecyclerView.ViewHolder {

        TextView rankTV;
        TextView primaryScoreTV;
        TextView secondaryScoreTV;
        TextView highestScoredWordTV;
        TextView timestampTV;

        public ScoreboardViewHolder(@NonNull View itemView) {
            super(itemView);
            rankTV = itemView.findViewById(R.id.sbRank);
            primaryScoreTV = itemView.findViewById(R.id.sbPrimaryScore);
            secondaryScoreTV = itemView.findViewById(R.id.sbSecondaryScore);
            highestScoredWordTV = itemView.findViewById(R.id.sbHighestScoredWord);
            timestampTV = itemView.findViewById(R.id.sbTimestamp);
        }

    }

    public void setOrderedByHighestWord(boolean orderedByHighestWord) {
        this.orderedByHighestWord = orderedByHighestWord;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

}
