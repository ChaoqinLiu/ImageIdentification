package com.example.sqlbrite.model;

import java.util.List;

public class TextResult {

    private int log_id;
    private int words_result_num;
    private List<WordsResult> wordsResults;

    public class WordsResult{
        public String words;

        public WordsResult(String words){
            this.words = words;
        }
    }
}
