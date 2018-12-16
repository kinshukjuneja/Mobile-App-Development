package edu.neu.madcourse.kinshukjuneja.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.utils.Trie;

public class ScroggleGameActivity extends AppCompatActivity {
    private static final String TITLE = "Scroggle";

    private MediaPlayer mediaPlayer;
    private Button muteButton;
    private Button pauseResumeButton;
    private TextView scoreTV;
    private TextView timerTV;
    private TextView phaseTV;
    private boolean isMuted;
    private Trie trie;
    private static final int[] MINI_BOARD_IDS = {
            R.id.mb00, R.id.mb01, R.id.mb02,
            R.id.mb10, R.id.mb11, R.id.mb12,
            R.id.mb20, R.id.mb21, R.id.mb22
    };
    private static final int[] SCORESHEET = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
    private static final String SCORE_TEMPLATE = "SCORE : %d";
    private static final String TIMER_TEMPLATE = "%02d:%02d";
    private static final int BONUS = 15;
    private MiniBoard[] miniBoards = new MiniBoard[9];
    private int activeMiniBoardId = -1;
    private List<String> guessedWords = new ArrayList<>();
    private int currentScore;
    private int highestScore;
    private CountDownTimer timer = null;
    private boolean isPaused;
    private long millisRemaining = 90000;
    private Button lastLetterSelected;
    private boolean phase2;
    private String word;
    private String highestScoredWord;
    private Stack<Button> selectedButtons = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroggle_game);
        setTitle(TITLE);

        muteButton = (Button)findViewById(R.id.muteButton);
        pauseResumeButton = (Button)findViewById(R.id.pauseResumeButton);
        scoreTV = (TextView)findViewById(R.id.scoreTV);
        timerTV = (TextView)findViewById(R.id.timerTV);
        phaseTV = (TextView)findViewById(R.id.phaseTV);

        initializeMiniBoards();
        startBackgroundMusic();
    }

    private void startTimer(long totalMillis) {
        if(totalMillis <= 15000) timerTV.setTextColor(Color.RED);
        else timerTV.setTextColor(getResources().getColor(R.color.holo_green_dark));
        timer = new CountDownTimer(totalMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                millisRemaining = millisUntilFinished;
                if(millisUntilFinished <= 15000) timerTV.setTextColor(Color.RED);
                timerTV.setText(String.format(TIMER_TEMPLATE,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                if(!phase2) {
                    finishAllMiniBoards();
                    trie.reset();
                    endPhase1();
                } else {
                    onClickEndGame(null);
                }
            }
        };
        timer.start();
    }

    private void endPhase1() {
        timer.cancel();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Congratulations for completing phase 1. Begin phase 2?");
        alertDialogBuilder.setPositiveButton("I'm ready",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        beginPhase2();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void beginPhase2() {
        phase2 = true;
        startTimer(90000);
        cleanUpMiniBoards();
        phaseTV.setText("PHASE 2");
    }

    private void cleanUpMiniBoards() {
        for(MiniBoard mb : miniBoards) {
            mb.beginPhase2();
        }
    }

    private void initializeMiniBoards() {
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < MINI_BOARD_IDS.length; ++i) {
            miniBoards[i] = (MiniBoard)fm.findFragmentById(MINI_BOARD_IDS[i]);
        }

        activateMiniBoards();
    }

    public boolean activateMiniBoards() {
        boolean atleastOneActive = false;
        for(MiniBoard mb : miniBoards) {
            if(mb.setActiveIfNotFinished()) atleastOneActive = true;
        }
        return atleastOneActive;
    }

    public void hasStartedSelection(int miniBoardId) {
        activeMiniBoardId = miniBoardId;
        deactivateMiniBoards(miniBoardId);
    }

    private void deactivateMiniBoards(int exception) {
        for(int i = 0; i < miniBoards.length; ++i) {
            if(i == exception) continue;
            miniBoards[i].setInactive();
        }
    }

    private void finishAllMiniBoards() {
        for(MiniBoard mb : miniBoards) mb.finishSelection(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTimer(millisRemaining);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    private void startBackgroundMusic() {
        mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor afd = getAssets().openFd("bgmusic.mp3");
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void muteMusic() {
        isMuted = true;
        muteButton.setBackgroundResource(R.drawable.unmute);
        mediaPlayer.setVolume(0,0);
    }

    private void unmuteMusic() {
        isMuted = false;
        muteButton.setBackgroundResource(R.drawable.mute);
        mediaPlayer.setVolume(1,1);
    }

    public void onClickMute(View view) {
        if(isMuted) unmuteMusic();
        else muteMusic();
    }

    public Trie getTrie() {
        if(trie != null) return trie;
        else {
            while(!Trie.isReadyForUse()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            trie = Trie.getSingletonRef();
            return trie;
        }
    }

    public void onClickConfirmWord(View view) {
        if(phase2) {
            if(lastLetterSelected != null) {
                boolean isValidWord = trie.validateLatestSearchNode();
                int score = 0;
                if(isValidWord) {
                    if(guessedWords.contains(word)) {
                        Toast.makeText(this, "You've already entered that word!!!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        guessedWords.add(word);
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                        score = calculateScore(word);
                        Toast.makeText(this, "That's a valid word. You earned " + score + " points.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    score = -5;
                    Toast.makeText(this, "That's an invalid word. You lost " + score + " points.",
                            Toast.LENGTH_SHORT).show();
                }
                trie.reset();
                clearPhase2SelectedButtons();
                if(score > highestScore) {
                    highestScore = score;
                    highestScoredWord = word;
                }
                currentScore += score;
                updateScoreField();
            }
        } else if(activeMiniBoardId != -1) {
            String word = miniBoards[activeMiniBoardId].getWord();
            boolean isValidWord = trie.validateLatestSearchNode();
            int score = 0;
            if(isValidWord) {
                guessedWords.add(word);
                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                score = calculateScore(word);
                Toast.makeText(this, "That's a valid word. You earned " + score + " points.",
                        Toast.LENGTH_SHORT).show();
            } else {
                score = -5;
                Toast.makeText(this, "That's an invalid word. You lost " + score + " points.",
                        Toast.LENGTH_SHORT).show();
            }
            if(score > highestScore) {
                highestScore = score;
                highestScoredWord = word;
            }
            currentScore += score;
            updateScoreField();

            miniBoards[activeMiniBoardId].finishSelection(false);
            if(!activateMiniBoards()) {
                endPhase1();
            }
            trie.reset();
        }
    }

    public void clearPhase2SelectedButtons() {
        selectedButtons = new Stack<>();
        lastLetterSelected = null;
        word = "";
        for(MiniBoard mb : miniBoards) {
            mb.beginPhase2();
        }
    }

    public void onClickPauseResume(View view) {
        if(isPaused) {
            pauseResumeButton.setBackgroundResource(R.drawable.pause);
            startTimer(millisRemaining);
            showMiniBoards();
            mediaPlayer.start();
            isPaused = false;
        } else {
            pauseResumeButton.setBackgroundResource(R.drawable.play);
            timer.cancel();
            hideMiniBoards();
            mediaPlayer.pause();
            isPaused = true;
        }
    }

    private void hideMiniBoards() {
        for(MiniBoard mb : miniBoards) mb.hideContent();
    }

    private void showMiniBoards() {
        for(MiniBoard mb : miniBoards) mb.showContent();
    }

    public int calculateScore(String word) {
        int score = 0;
        if(word.length() == 9) score += BONUS;
        for(int i = 0; i < word.length(); ++i) score += SCORESHEET[word.charAt(i) - 'a'];

        return score;
    }

    private void updateScoreField() {
        scoreTV.setText(String.format(SCORE_TEMPLATE, currentScore));
    }

    public void notifyPhase2Selection(Button letterPressed) {
        String letterText = letterPressed.getText().toString();
        trie.searchWithNewCharacterSuffix(letterText.charAt(0));
        letterPressed.setTextColor(Color.WHITE);
        letterPressed.setBackgroundResource(R.drawable.boxbackselectedlatest);
        word += letterText;
        selectedButtons.push(letterPressed);

        if(lastLetterSelected != null) {
            clearLastLetterSelectionBorder();
        }
        lastLetterSelected = letterPressed;
    }

    private void clearLastLetterSelectionBorder() {
        lastLetterSelected.setBackgroundResource(R.drawable.boxbackselected);
    }

    public void onClickEndGame(View view) {
        Intent intent = new Intent(this, ScroggleEndActivity.class);
        intent.putExtra("totalScore", currentScore);
        intent.putExtra("highestScore", highestScore);
        intent.putExtra("highestScoredWord", highestScoredWord);
        intent.putStringArrayListExtra("guessedWords", (ArrayList<String>) guessedWords);
        startActivity(intent);
    }

}
