package edu.neu.madcourse.kinshukjuneja.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.utils.Trie;

public class DictionaryActivity extends AppCompatActivity {

    private EditText wordSearchTV;
    private TextView wordListTV;
    private Trie trie;
    private boolean isInvalidPrefix;
    private static final String TITLE = "Test Dictionary";
    private static final String EMPTY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        while(!Trie.isReadyForUse()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        trie = Trie.getSingletonRef();
        setTitle(TITLE);
        setContentView(R.layout.activity_dictionary);
        wordSearchTV = (EditText)findViewById(R.id.wordSearch);
        wordListTV = (TextView)findViewById(R.id.wordList);
        attachTextChangedListener();
        wordListTV.setMovementMethod(new ScrollingMovementMethod());
    }

    public void onClickClear(View view) {
        wordSearchTV.setText(EMPTY);
        wordListTV.setText(EMPTY);
        trie.reset();
    }

    public void onClickAcknowledgments(View view) {
        Intent intent = new Intent(this, DictionaryAcknowledgmentsActivity.class);
        startActivity(intent);
    }

    private void attachTextChangedListener() {
        wordSearchTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = charSequence.length();
                if(len == 0) return;
                char enteredChar = charSequence.charAt(len - 1);
                trie.searchWithNewCharacterSuffix(enteredChar);
                validateLatestSearchNode(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }



    private void validateLatestSearchNode(String word) {
        if(trie.validateLatestSearchNode()) {
            wordListTV.append(word + "\n");

            // ref : https://stackoverflow.com/questions/12154940/how-to-make-a-beep-in-android
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
        }
    }
}
