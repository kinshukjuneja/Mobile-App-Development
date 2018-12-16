package edu.neu.madcourse.kinshukjuneja.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.utils.ScroggleFirebaseHelper;

public class ScroggleChangeUsernameActivity extends AppCompatActivity {

    private EditText newUsernameTV;
    private Button changeUsernameB;
    private ScroggleFirebaseHelper scroggleFirebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroggle_change_username);
        newUsernameTV = (EditText)findViewById(R.id.newUsername);
        changeUsernameB = (Button)findViewById(R.id.changeUsername);
        scroggleFirebaseHelper = ScroggleFirebaseHelper.getSingletonRef();
    }

    public void onClickChangeUsername(View view) {
        String text = newUsernameTV.getText().toString();
        if(text.length() == 0 || (text.length() >= 5 && "Guest".equals(text.subSequence(0,5)))) {
            Toast.makeText(this, "Invalid username", Toast.LENGTH_LONG).show();
            newUsernameTV.setText("");
            newUsernameTV.setHint("enter here");
        } else {
            scroggleFirebaseHelper.updateUsername(text, this);
        }
    }
}
