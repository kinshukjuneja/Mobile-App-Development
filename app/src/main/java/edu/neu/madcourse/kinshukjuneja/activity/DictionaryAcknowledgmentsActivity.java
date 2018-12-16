package edu.neu.madcourse.kinshukjuneja.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.neu.madcourse.kinshukjuneja.R;

public class DictionaryAcknowledgmentsActivity extends AppCompatActivity {
    private static final String TITLE = "Acknowledgment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_acknowledgments);
        setTitle(TITLE);
    }
}
