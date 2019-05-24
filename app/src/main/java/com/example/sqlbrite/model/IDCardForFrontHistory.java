package com.example.sqlbrite.model;

import java.util.List;


public class IDCardForFrontHistory {

    public List<IDCardForFrontHistoryArray> result;

    public class IDCardForFrontHistoryArray {

        public int id;
        public String name;
        public String number;
        public byte[] bytes;

        public IDCardForFrontHistoryArray(int id,String name,String number,byte[] bytes) {
            this.id = id;
            this.name = name;
            this.number = number;
            this.bytes = bytes;
        }
    }
}
