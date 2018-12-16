package edu.neu.madcourse.kinshukjuneja.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.utils.Trie;

import java.util.Stack;

/**
 * A simple {@link Fragment} subclass.
 */
public class MiniBoard extends Fragment {

    private static final int[] LETTER_IDS = {
            R.id.letter00, R.id.letter01, R.id.letter02,
            R.id.letter10, R.id.letter11, R.id.letter12,
            R.id.letter20, R.id.letter21, R.id.letter22
    };

    private static final int[][] PATTERNS = {
            {0, 1, 2, 5, 4, 3, 6, 7, 8},
            {2, 1, 0, 3, 4, 5, 8, 7, 6}
    };

    private boolean[] lettersSelected = new boolean[9];
    private int lastLetterSelected = -1;
    private Trie trie;
    private String word = "";
    private boolean isActive;
    private boolean didNotifyGame;
    private boolean isFinished;
    private ScroggleGameActivity gameActivity;
    private View rootView;
    private boolean phase2;
    private Stack<Integer> letterTagsSelected = new Stack<>();

    public MiniBoard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mini_board,
                container, false);

        gameActivity = (ScroggleGameActivity)getActivity();
        trie = gameActivity.getTrie();
        attachListeners();
        populateMiniBoard();
        return rootView;
    }

    private void populateMiniBoard() {
        int randomPattern = (int)(Math.random() * PATTERNS.length);
        String word = trie.fetchAndRemoveNineLetterWord();

        for(int i = 0; i < 9; ++i) {
            Button button = (Button)rootView.findViewById(LETTER_IDS[PATTERNS[randomPattern][i]]);
            button.setText(String.valueOf(word.charAt(i)));
        }
    }

    private void attachListeners() {
        for(int letterId : LETTER_IDS) {
            Button letter = (Button)rootView.findViewById(letterId);
            letter.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View button) {
                    if(phase2) {
                        Button letterPressed = (Button) button;
                        String letterText = letterPressed.getText().toString();
                        int tag = Integer.parseInt((String)letterPressed.getTag());
                        if(letterText.length() == 1 && isValidSelectionForPhase2(tag)) {
                            gameActivity.notifyPhase2Selection(letterPressed);
                        }
                    } else if(isActive) {
                        if(!didNotifyGame) {
                            gameActivity.hasStartedSelection(Integer.parseInt(getTag()));
                            didNotifyGame = true;
                        }
                        Button letterPressed = (Button) button;
                        int tag = Integer.parseInt((String)letterPressed.getTag());
                        if(isValidSelection(tag)) {
                            String letterText = letterPressed.getText().toString();
                            word += letterText;
                            trie.searchWithNewCharacterSuffix(letterText.charAt(0));
                            letterPressed.setTextColor(Color.WHITE);
                            letterPressed.setBackgroundResource(R.drawable.boxbackselectedlatest);
                            if(lastLetterSelected != -1) {
                                clearLastLetterSelectionBorder();
                            }
                            lastLetterSelected = tag;
                        } else if(lastLetterSelected == tag) {
                            if(word.length() == trie.getPrefixLength()) {
                                trie.returnBack();
                            }
                            letterTagsSelected.pop();
                            lettersSelected[lastLetterSelected] = false;
                            letterPressed.setTextColor(Color.BLACK);
                            letterPressed.setBackgroundResource(R.drawable.boxbackunselected);
                            lastLetterSelected = letterTagsSelected.size() == 0 ? -1 : letterTagsSelected.peek();
                            if(lastLetterSelected != -1) {
                                ((Button)rootView.findViewById(LETTER_IDS[lastLetterSelected])).setTextColor(Color.WHITE);
                                ((Button)rootView.findViewById(LETTER_IDS[lastLetterSelected])).setBackgroundResource(R.drawable.boxbackselectedlatest);
                            }
                            word = word.substring(0, word.length() - 1);
                            if(word.length() == 0) {
                                gameActivity.activateMiniBoards();
                                trie.reset();
                            } else if(word.length() == trie.getPrefixLength()) {
                                trie.setInvalidPrefix(false);
                            }

                        }
                    }
                }

            });
        }
    }

    private void clearLastLetterSelectionBorder() {
        Button lastSelectedButton = (Button)rootView.findViewById(LETTER_IDS[lastLetterSelected]);
        lastSelectedButton.setBackgroundResource(R.drawable.boxbackselected);
    }

    private boolean isValidSelection(int tag) {
        if(lettersSelected[tag] || !isAdjacentToLastSelected(tag)) return false;
        letterTagsSelected.push(tag);
        lettersSelected[tag] = true;
        return true;
    }

    private boolean isValidSelectionForPhase2(int tag) {
        if(lettersSelected[tag]) return false;
        lettersSelected[tag] = true;
        return true;
    }

    private boolean isAdjacentToLastSelected(int tag) {
        if(lastLetterSelected == -1) return true;
        int lastSelectedLetterRow = lastLetterSelected / 3;
        int lastSelectedLetterCol = lastLetterSelected % 3;
        int currSelectedLetterRow = tag / 3;
        int currSelectedLetterCol = tag % 3;
        return Math.abs(lastSelectedLetterRow - currSelectedLetterRow) < 2 && Math.abs(lastSelectedLetterCol - currSelectedLetterCol) < 2;
    }

    public String getWord() {
        return word;
    }

    public boolean setActiveIfNotFinished() {
        if(!isFinished) {
            isActive = true;
            return true;
        }
        return false;
    }

    public void setInactive() {
        isActive = false;
    }

    public void finishSelection(boolean forceQuitActiveSelections) {
        if(forceQuitActiveSelections && isActive) {
            for(int i = 0; i < lettersSelected.length; ++i) {
                Button button = (Button)rootView.findViewById(LETTER_IDS[i]);
                button.setText("");
                button.setBackgroundResource(R.drawable.boxbackunselected);
            }
        } else if(!isFinished) {
            isActive = false;
            didNotifyGame = false;
            isFinished = true;
            for(int i = 0; i < lettersSelected.length; ++i) {
                if(!lettersSelected[i]) {
                    Button button = (Button)rootView.findViewById(LETTER_IDS[i]);
                    button.setText("");
                }
            }
            if(lastLetterSelected != -1) clearLastLetterSelectionBorder();
        }
        lettersSelected = new boolean[9];
        lastLetterSelected = -1;
        word = "";
    }

    public void hideContent() {
        for(int i = 0; i < lettersSelected.length; ++i) {
            Button button = (Button)rootView.findViewById(LETTER_IDS[i]);
            button.setVisibility(View.INVISIBLE);
        }
    }

    public void showContent() {
        for(int i = 0; i < lettersSelected.length; ++i) {
            Button button = (Button)rootView.findViewById(LETTER_IDS[i]);
            button.setVisibility(View.VISIBLE);
        }
    }

    public void beginPhase2() {
        phase2 = true;
        for(int i = 0; i < lettersSelected.length; ++i) {
            lettersSelected[i] = false;
            Button button = (Button)rootView.findViewById(LETTER_IDS[i]);
            button.setTextColor(Color.BLACK);
            button.setBackgroundResource(R.drawable.boxbackunselected);
        }
    }

}
