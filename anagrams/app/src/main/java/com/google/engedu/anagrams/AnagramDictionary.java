package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import android.util.Log;


public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;

    private int wordLength = 4;

    private Random random = new Random();

    private ArrayList<String> wordList; // storing all the word from words.txt into wordlist for future uses
    private HashSet<String> wordSet; // allow us to rapidly (in O(1)) verify whether a word is valid
    private HashMap<String, ArrayList<String>> lettersToWord; //
    private HashMap<Integer, ArrayList<String>> sizeToWords;

    //Each word that is read from the dictionary file should be stored in anArrayList (called wordList).
    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;

        wordList = new ArrayList<String>();
        wordSet = new HashSet<String>();
        lettersToWord = new HashMap<String, ArrayList<String>>();

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedWord = sortLetters(word); //base

            if (sizeToWords.containsKey(word.length())){
                ArrayList<String> temp = sizeToWords.get(word.length());
                temp.add(word);
                sizeToWords.put(word.length(),temp);
            }
            else{
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(word);
                sizeToWords.put(word.length(),temp);
            }

            if (lettersToWord.containsKey(sortedWord)){
                ArrayList<String> listOfWord = lettersToWord.get(sortedWord);
                listOfWord.add(word);
                lettersToWord.put(sortedWord,listOfWord);
            }
            else{
                ArrayList<String> listofWord = new ArrayList<String>();
                listofWord.add(word);
                lettersToWord.put(sortedWord,listofWord);
            }

        }
    }

    public boolean isGoodWord(String word, String base) {

        return (wordSet.contains(word)) && !(wordSet.contains(base));
    }

    public String sortLetters(String word){

        String sortedWord;
        char[] letter = word.toCharArray();
        Arrays.sort(letter);
        sortedWord = letter.toString();
        return sortedWord;
    }

    //takes a string and finds all the anagrams of that string in our input.
    public ArrayList<String> getAnagrams(String targetWord) {
        /*
        ArrayList<String> result = new ArrayList<String>(); // store any anagram into the result
        targetWord = sortLetters(targetWord);

        for (String word: wordList){
            if (sortLetters(word).equals(targetWord)){
                Log.v("result added", word);
                result.add(word);

            }
        }
        return result;
        */

        return lettersToWord.get(sortLetters(targetWord));
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        char[] alphabets = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (char letter: alphabets){
            if (lettersToWord.containsKey(sortLetters(word + letter))){
                ArrayList<String> temp= lettersToWord.get(sortLetters(word + letter));
                for (String s:temp){
                    Log.v("add one more letter:",s);
                    result.add(s);
                }
            }
            }


        return result;
    }


    //pick a word randomly
    public String pickGoodStarterWord() {
        int length = 0;
        ArrayList<String> temp = sizeToWords.get(wordLength);
        Integer wordsSize = temp.size();

        while (length < MIN_NUM_ANAGRAMS){
            String word = temp.get(new Random().nextInt(wordsSize));
            length = getAnagramsWithOneMoreLetter(word).size();
            if (length >= MIN_NUM_ANAGRAMS){
                if (wordLength < MAX_WORD_LENGTH) wordLength ++;
                return word;
            }
        }
        return "stop";
    }
}

