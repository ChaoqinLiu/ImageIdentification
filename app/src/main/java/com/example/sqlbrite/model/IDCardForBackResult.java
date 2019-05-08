package com.example.sqlbrite.model;

public class IDCardForBackResult {

    private String dateOfIssue;
    private String issuingAuthority;
    private String expirationDate;

    public void setDateOfIssue(String dateOfIssue){
        this.dateOfIssue = dateOfIssue;
    }

    public String getDateOfIssue(){
        return dateOfIssue;
    }

    public void setIssuingAuthority(String issuingAuthority){
        this.issuingAuthority = issuingAuthority;
    }

    public String getIssuingAuthority(){
        return issuingAuthority;
    }

    public void setExpirationDate(String expirationDate){
        this.expirationDate = expirationDate;
    }

    public String getExpirationDate(){
        return expirationDate;
    }

}
