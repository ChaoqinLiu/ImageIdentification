package com.example.sqlbrite.model;

import java.util.List;


public class DriverLicenseHistory {

    public List<DriverLicenseHistoryArray> result;

    public class DriverLicenseHistoryArray {

        public int id;
        public String name;
        public String number;
        public byte[] bytes;

        public DriverLicenseHistoryArray(int id,String name,String number,byte[] bytes) {
            this.id = id;
            this.name = name;
            this.number = number;
            this.bytes = bytes;
        }
    }
}
