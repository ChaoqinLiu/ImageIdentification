package com.example.sqlbrite.model;

import com.google.gson.annotations.SerializedName;

public class IDCardForFrontResult {

    public IDCardForFrontResultWordsResult words_result;

    public class IDCardForFrontResultWordsResult {

        @SerializedName("住址")
        public Address address;

        @SerializedName("出生")
        public Birthday birthday;

        @SerializedName("姓名")
        public UserName userName;

        @SerializedName("公民身份号码")
        public IDNumber idNumber;

        @SerializedName("性别")
        public Gender gender;

        @SerializedName("民族")
        public Nationality nationality;

        public class Address {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class Birthday {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class UserName {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class IDNumber {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class Gender {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class Nationality {
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
