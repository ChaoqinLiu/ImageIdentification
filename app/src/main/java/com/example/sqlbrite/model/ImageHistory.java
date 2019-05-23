package com.example.sqlbrite.model;

import java.util.List;

public class ImageHistory {

    public int log_id;
    public int result_code;
    public List<ImageHistoryArray> result;

    public class ImageHistoryArray {

        public int id;
        public float score;
        public String root;
        public String keyword;
        public byte[] bytes;

        public ImageHistoryArray(int id,float score, String root, String keyword, byte[] bytes) {
            this.id = id;
            this.score = score;
            this.root = root;
            this.keyword = keyword;
            this.bytes = bytes;
        }
    }
}
