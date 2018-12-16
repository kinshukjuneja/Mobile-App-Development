package edu.neu.madcourse.kinshukjuneja.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.neu.madcourse.kinshukjuneja.R;

public class AboutActivity extends AppCompatActivity {
    private static final String TITLE = "Kinshuk Juneja";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        setContentView(R.layout.activity_about);
    }
}
