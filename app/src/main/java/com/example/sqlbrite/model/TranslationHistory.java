package com.example.sqlbrite.model;

import java.util.List;

public class TranslationHistory {

    public List<TranslationHistoryArray> result;

    public class TranslationHistoryArray {

        public int id;
        public String original;
        public String translation;
        public byte[] bytes;

        public TranslationHistoryArray(int id, String original, String translation, byte[] bytes) {
            this.id = id;
            this.original = original;
            this.translation = translation;
            this.bytes = bytes;
        }
    }
}
