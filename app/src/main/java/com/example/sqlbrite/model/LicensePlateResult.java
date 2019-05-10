package com.example.sqlbrite.model;

public class LicensePlateResult {

    public LicensePlateWordsResult words_result;

    public class LicensePlateWordsResult {

        private String color;
        private String number;

        public void setColor(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getNumber() {
            return number;
        }
    }
}
