package com.example.sqlbrite.model;

public class BankCardResult {

    public BankCardResultArray result;

    public class BankCardResultArray {

        private String bank_card_number;
        private String valid_date;
        private String bank_name;
        private String bank_card_type;


        public void setBankCardNumber(String bank_card_number) {
            this.bank_card_number = bank_card_number;
        }

        public String getBankCardNumber() {
            return bank_card_number;
        }

        public void setValidDate(String valid_date) {
            this.valid_date = valid_date;
        }

        public String getValidDate() {
            return valid_date;
        }

        public void setBankName(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getBankName() {
            return bank_name;
        }

        public void setBankCardType(String bank_card_type) {
            this.bank_card_type = bank_card_type;
        }

        public String getBankCardType() {
            return bank_card_type;
        }
    }

}
