package com.example.sqlbrite.model;

public class BankCardResult {

    public BankCardResultArray result;

    public class BankCardResultArray {

        public String bank_card_number;
        public String valid_date;
        public String bank_name;
        public String bank_card_type;


        public void setBank_card_number(String bank_card_number) {
            this.bank_card_number = bank_card_number;
        }

        public String getBank_card_number() {
            return bank_card_number;
        }

        public void setValid_date(String valid_date) {
            this.valid_date = valid_date;
        }

        public String getValid_date() {
            return valid_date;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_card_type(String bank_card_type) {
            this.bank_card_type = bank_card_type;
        }

        public String getBank_card_type() {
            return bank_card_type;
        }
    }

}
