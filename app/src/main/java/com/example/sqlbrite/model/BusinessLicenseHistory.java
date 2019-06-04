package com.example.sqlbrite.model;

import java.util.List;


public class BusinessLicenseHistory {

    public List<BusinessLicenseHistoryArray> result;

    public class BusinessLicenseHistoryArray {

        public int id;
        public String companyName;
        public String legalPerson;
        public byte[] bytes;

        public BusinessLicenseHistoryArray(int id,String companyName,String legalPerson,byte[] bytes) {
            this.id = id;
            this.companyName = companyName;
            this.legalPerson = legalPerson;
            this.bytes = bytes;
        }
    }
}
