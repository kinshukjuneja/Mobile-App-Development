package edu.neu.madcourse.kinshukjuneja.asynctask;

import android.os.AsyncTask;

import edu.neu.madcourse.kinshukjuneja.utils.Trie;

public class DictionaryLoaderTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        Trie.loadWordListInMemory();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Trie.setReadyForUse();
    }

}
