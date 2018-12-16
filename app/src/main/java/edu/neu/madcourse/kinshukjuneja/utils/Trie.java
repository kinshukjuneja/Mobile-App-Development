package edu.neu.madcourse.kinshukjuneja.utils;

import android.media.AudioManager;
import android.media.ToneGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Trie {

    private TrieNode root;
    private TrieNode latestSearchNode;
    private List<String> nineLetterWords;
    private boolean isInvalidPrefix;
    private int prefixLength;

    private static Trie singletonRef;
    private static boolean readyForUse;

    private Trie() {
        root = new TrieNode(' ', null);
        latestSearchNode = root;
        nineLetterWords = new ArrayList<>();
    }

    public static void loadWordListInMemory() {
        singletonRef = new Trie();

        // ref : https://stackoverflow.com/questions/9544737/read-file-from-assets
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(Trie.class.getResourceAsStream("/assets/wordlist.txt")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                singletonRef.loadWordInMemory(mLine);
                if(mLine.length() == 9) singletonRef.nineLetterWords.add(mLine);
            }
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void setReadyForUse() {
        readyForUse = true;
    }

    public static boolean isReadyForUse() {
        return readyForUse;
    }

    public static Trie getSingletonRef() {
        return singletonRef;
    }

    private void loadWordInMemory(String word) {
        TrieNode currNode = root;
        for(int i = 0; i < word.length(); ++i) {
            currNode = currNode.addAndGetNextChar(word.charAt(i));
        }
        currNode.setValidWord();
    }

    public TrieNode getLatestSearchNode() {
        return latestSearchNode;
    }

    public void setLatestSearchNode(TrieNode latestSearchNode) {
        this.latestSearchNode = latestSearchNode;
    }

    public void reset() {
        this.latestSearchNode = root;
        this.isInvalidPrefix = false;
        this.prefixLength = 0;
    }

    public boolean isValidWord() {
        return latestSearchNode.isValidWord();
    }

    public boolean moveToNextChar(char newChar) {
        TrieNode nextNode = latestSearchNode.getNextChar(newChar);
        if(nextNode != null) {
            ++prefixLength;
            latestSearchNode = nextNode;
            return true;
        }
        return false;
    }

    public String fetchAndRemoveNineLetterWord() {
        int randomIndex = (int)(Math.random() * nineLetterWords.size());
        String randomWord = nineLetterWords.get(randomIndex);
        nineLetterWords.remove(randomIndex);
        return randomWord;
    }

    public void searchWithNewCharacterSuffix(char newChar) {
        if(!isInvalidPrefix && !moveToNextChar(newChar)) isInvalidPrefix = true;
    }

    public boolean validateLatestSearchNode() {
        if(!isInvalidPrefix && isValidWord()) return true;
        return false;
    }

    public void returnBack() {
        latestSearchNode = latestSearchNode.getParent();
        --prefixLength;
    }

    public int getPrefixLength() {
        return prefixLength;
    }

    public void setInvalidPrefix(boolean isInvalidPrefix) {
        this.isInvalidPrefix = isInvalidPrefix;
    }

}
