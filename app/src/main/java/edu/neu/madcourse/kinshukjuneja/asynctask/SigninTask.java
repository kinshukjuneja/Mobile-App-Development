package edu.neu.madcourse.kinshukjuneja.asynctask;

import android.os.AsyncTask;

import edu.neu.madcourse.kinshukjuneja.utils.ScroggleFirebaseHelper;

public class SigninTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        ScroggleFirebaseHelper scroggleFirebaseHelper = ScroggleFirebaseHelper.getSingletonRef();
        scroggleFirebaseHelper.signInAnonymously();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

}