package edu.neu.madcourse.kinshukjuneja.utils;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {

    private char character;
    private Map<Character, TrieNode> nextNodes;
    private boolean validWord;
    private TrieNode parent;

    public TrieNode(char character, TrieNode parent) {
        this.character = character;
        this.nextNodes = new HashMap<>();
        this.parent = parent;
    }

    public TrieNode getNextChar(char nextChar) {
        return nextNodes.get(nextChar);
    }

    public TrieNode addAndGetNextChar(char nextChar) {
        if(nextNodes.containsKey(nextChar)) return nextNodes.get(nextChar);
        nextNodes.put(nextChar, new TrieNode(nextChar, this));
        return nextNodes.get(nextChar);
    }

    public void setValidWord() {
        validWord = true;
    }

    public boolean isValidWord() {
        return validWord;
    }

    public TrieNode getParent() {
        return parent;
    }

    public char getCharacter() {
        return character;
    }

}
