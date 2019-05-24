package com.example.sqlbrite.model;

import java.util.List;


public class IDCardForBackHistory {

    public List<IDCardForBackHistoryArray> result;

    public class IDCardForBackHistoryArray {

        public int id;
        public String issuingAuthority;
        public String dateOfIssue;
        public String expirationDate;
        public byte[] bytes;

        public IDCardForBackHistoryArray(int id,String issuingAuthority,String dateOfIssue,String expirationDate,byte[] bytes) {
            this.id = id;
            this.issuingAuthority = issuingAuthority;
            this.dateOfIssue = dateOfIssue;
            this.expirationDate = expirationDate;
            this.bytes = bytes;
        }
    }
}
