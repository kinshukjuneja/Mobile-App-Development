package edu.neu.madcourse.kinshukjuneja.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import edu.neu.madcourse.kinshukjuneja.utils.ScroggleFirebaseHelper;

public class LeaderboardTask extends AsyncTask<Activity, Void, Void> {

    @Override
    protected Void doInBackground(Activity... activities) {
        ScroggleFirebaseHelper scroggleFirebaseHelper = ScroggleFirebaseHelper.getSingletonRef();
        scroggleFirebaseHelper.buildInitialLeaderboard(activities[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

}
