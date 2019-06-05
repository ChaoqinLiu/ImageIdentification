package com.example.sqlbrite.model;

import java.util.List;


public class LicensePlateHistory {

    public List<LicensePlateHistoryArray> result;

    public class LicensePlateHistoryArray {

        public int id;
        public String color;
        public String number;
        public byte[] bytes;

        public LicensePlateHistoryArray(int id,String color,String number,byte[] bytes) {
            this.id = id;
            this.color = color;
            this.number = number;
            this.bytes = bytes;
        }
    }
}
