package com.example.sqlbrite.model;

import com.google.gson.annotations.SerializedName;

public class IDCardForBackResult {

    public IDCardForBackResultWordsResult words_result;

    public class IDCardForBackResultWordsResult {

        @SerializedName("签发日期")
        public DateOfIssue dateOfIssue;

        @SerializedName("签发机关")
        public IssuingAuthority issuingAuthority;

        @SerializedName("失效日期")
        public ExpirationDate expirationDate;

        public class DateOfIssue {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class IssuingAuthority {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class ExpirationDate {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }
    }

}
