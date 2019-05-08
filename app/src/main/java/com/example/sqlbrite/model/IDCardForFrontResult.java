package com.example.sqlbrite.model;

public class IDCardForFrontResult {

    private String address;
    private String birthday;
    private String userName;
    private String idNumber;
    private String gender;
    private String nationality;

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return address;
    }

    public void setBirthday(String birthday){
        this.birthday = birthday;
    }

    public String getBirthday(){
        return birthday;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return userName;
    }

    public void setIdNumber(String idNumber){
        this.idNumber = idNumber;
    }

    public String getIdNumber(){
        return idNumber;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public String getGender(){
        return gender;
    }

    public void setNationality(String nationality){
        this.nationality = nationality;
    }

    public String getNationality(){
        return nationality;
    }


}
