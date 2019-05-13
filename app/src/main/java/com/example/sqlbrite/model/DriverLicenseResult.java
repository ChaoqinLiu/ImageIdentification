package com.example.sqlbrite.model;

import com.google.gson.annotations.SerializedName;

public class DriverLicenseResult {

    public DriverLicenseResultWordsResult words_result;

    public class DriverLicenseResultWordsResult {

        @SerializedName("住址")
        public Address address;

        @SerializedName("出生日期")
        public Birthday birthday;

        @SerializedName("姓名")
        public UserName userName;

        @SerializedName("证号")
        public CertificateNumber certificateNumber;

        @SerializedName("性别")
        public Gender gender;

        @SerializedName("国籍")
        public CountryOfCitizenship countryOfCitizenship;

        @SerializedName("准驾车型")
        public QuasiDrivingModel quasiDrivingModel;

        @SerializedName("初次领证日期")
        public InitialLicenseDate initialLicenseDate;

        @SerializedName("有效期限")
        public ValidityPeriod validityPeriod;

        @SerializedName("至")
        public To to;

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

        public class CertificateNumber {
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

        public class CountryOfCitizenship {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class QuasiDrivingModel {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class InitialLicenseDate {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class ValidityPeriod {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class To {
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
