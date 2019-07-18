package com.softtech.cognitosnslibrary.model;

public class SignUpInfoModel {

    private String userId = null;
    private String fullPhoneNumber = null;
    private String email = null;
    private String name = null;
    private String password = null;

    public SignUpInfoModel(String userId, String fullPhoneNumber, String email, String name, String password){
        this.userId = userId;
        this.fullPhoneNumber = fullPhoneNumber;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getFullPhoneNumber() {
        return fullPhoneNumber;
    }

    public void setFullPhoneNumber(String fullPhoneNumber) {
        this.fullPhoneNumber = fullPhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
