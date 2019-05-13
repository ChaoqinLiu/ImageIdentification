package com.example.sqlbrite.model;

import com.google.gson.annotations.SerializedName;

public class DrivingLicenseResult {

    public DriverLicenseResultWordsResult words_result;

    public class DriverLicenseResultWordsResult {

        @SerializedName("住址")
        public Address address;

        @SerializedName("号牌号码")
        public NumberPlateNumber numberPlateNumber;

        @SerializedName("车辆类型")
        public VehicleType vehicleType;

        @SerializedName("所有人")
        public Owner owner;

        @SerializedName("使用性质")
        public NatureOfUse natureOfUse;

        @SerializedName("品牌型号")
        public BrandModelNumber brandModelNumber;

        @SerializedName("发动机号码")
        public EngineNumber engineNumber;

        @SerializedName("车辆识别代号")
        public VehicleIdentificationNumber vehicleIdentificationNumber;

        @SerializedName("注册日期")
        public RegistrationDate registrationDate;

        @SerializedName("发证日期")
        public IssuingCertificateOfDate issuingCertificateOfDate;

        public class Address {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class NumberPlateNumber {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class VehicleType {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class Owner {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class NatureOfUse {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class BrandModelNumber {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class EngineNumber {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class VehicleIdentificationNumber {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class RegistrationDate {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class IssuingCertificateOfDate {
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
