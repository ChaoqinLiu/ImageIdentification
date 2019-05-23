package com.example.sqlbrite.model;

import java.util.List;

public class TextHistory {

    public List<TextHistoryArray> result;

    public class TextHistoryArray {

        public int id;
        public String words;
        public byte[] bytes;

        public TextHistoryArray(int id, String words, byte[] bytes) {
            this.id = id;
            this.words = words;
            this.bytes = bytes;
        }
    }
}
