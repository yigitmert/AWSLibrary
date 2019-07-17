package com.softtech.cognitosnslibrary.model;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;

public class LoginSnsResponseModel {

    private CognitoUserSession cognitoUserSession = null;
    private String snsArnId = null;

    public LoginSnsResponseModel(CognitoUserSession cognitoUserSession, String snsArnId){
        this.cognitoUserSession = cognitoUserSession;
        this.snsArnId = snsArnId;
    }

    public CognitoUserSession getCognitoUserSession() {
        return cognitoUserSession;
    }

    public void setCognitoUserSession(CognitoUserSession cognitoUserSession) {
        this.cognitoUserSession = cognitoUserSession;
    }

    public String getSnsArnId() {
        return snsArnId;
    }

    public void setSnsArnId(String snsArnId) {
        this.snsArnId = snsArnId;
    }
}
