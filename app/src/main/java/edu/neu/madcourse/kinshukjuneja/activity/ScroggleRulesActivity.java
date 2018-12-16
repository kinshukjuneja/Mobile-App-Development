package edu.neu.madcourse.kinshukjuneja.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import edu.neu.madcourse.kinshukjuneja.R;

public class ScroggleRulesActivity extends AppCompatActivity {
    private static final String TITLE = "Rules";
    private TextView rulesTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroggle_rules);
        setTitle(TITLE);

        rulesTV = (TextView)findViewById(R.id.rulesTV);
        rulesTV.setMovementMethod(new ScrollingMovementMethod());
    }
}
