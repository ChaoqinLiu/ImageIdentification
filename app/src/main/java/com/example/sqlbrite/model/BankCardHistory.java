package com.example.sqlbrite.model;

import java.util.List;


public class BankCardHistory {

    public List<BankCardHistoryArray> result;

    public class BankCardHistoryArray {

        public int id;
        public String name;
        public String number;
        public byte[] bytes;

        public BankCardHistoryArray(int id,String name,String number,byte[] bytes) {
            this.id = id;
            this.name = name;
            this.number = number;
            this.bytes = bytes;
        }
    }
}
