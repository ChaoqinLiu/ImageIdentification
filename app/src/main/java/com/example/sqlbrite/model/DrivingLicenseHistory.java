package com.example.sqlbrite.model;

import java.util.List;


public class DrivingLicenseHistory {

    public List<DrivingLicenseHistoryArray> result;

    public class DrivingLicenseHistoryArray {

        public int id;
        public String address;
        public String owner;
        public byte[] bytes;

        public DrivingLicenseHistoryArray(int id,String address,String owner,byte[] bytes) {
            this.id = id;
            this.address = address;
            this.owner = owner;
            this.bytes = bytes;
        }
    }
}
