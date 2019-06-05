package com.example.sqlbrite.model;

import java.util.List;


public class PassportHistory {

    public List<PassportHistoryArray> result;

    public class PassportHistoryArray {

        public int id;
        public String number;
        public String name;
        public byte[] bytes;

        public PassportHistoryArray(int id,String number,String name,byte[] bytes) {
            this.id = id;
            this.number = number;
            this.name = name;
            this.bytes = bytes;
        }
    }
}
