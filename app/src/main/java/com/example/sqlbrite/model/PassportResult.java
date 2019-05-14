package com.example.sqlbrite.model;

import com.google.gson.annotations.SerializedName;

public class PassportResult {

    public PassportResultWordsResult words_result;

    public class PassportResultWordsResult {

        @SerializedName("国家码")
        public CountryCode countryCode;

        @SerializedName("护照签发地点")
        public PassportIssuanceLocation passportIssuanceLocation;

        @SerializedName("有效期至")
        public ValidUntil validUntil;

        @SerializedName("护照号码")
        public PassportNumber passportNumber;

        @SerializedName("签发日期")
        public PassportDateOfIssue passportDateOfIssue;

        @SerializedName("出生地点")
        public BirthPlace birthPlace;

        @SerializedName("姓名")
        public OwnerName ownerName;

        @SerializedName("姓名拼音")
        public PinyinOfName pinyinOfName;

        @SerializedName("生日")
        public OwnerBirthday ownerBirthday;

        @SerializedName("性别")
        public Sex sex;

        public class CountryCode {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class PassportIssuanceLocation {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class ValidUntil {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class PassportNumber {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class PassportDateOfIssue {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class BirthPlace {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class OwnerName {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class PinyinOfName {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class OwnerBirthday {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class Sex {
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
