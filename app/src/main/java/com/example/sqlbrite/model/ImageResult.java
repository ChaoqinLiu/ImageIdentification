package com.example.sqlbrite.model;

import java.util.List;

public class ImageResult {

    public int log_id;
    public int result_code;
    public List<ResultArray> result;

    public class ResultArray {

        public float score;
        public String root;
        public String keyword;

        public ResultArray(float score, String root, String keyword) {
            this.score = score;
            this.root = root;
            this.keyword = keyword;
        }
    }
}
