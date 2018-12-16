package edu.neu.madcourse.kinshukjuneja.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.neu.madcourse.kinshukjuneja.R;

public class ScroggleAcknowledgmentsActivity extends AppCompatActivity {
    private static final String TITLE = "Acknowledgment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroggle_acknowledgments);
        setTitle(TITLE);
    }
}
