package com.softtech.cognitosnslibrary.model;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;

public class SignUpResponseModel {

    private CognitoUser cognitoUser = null;
    private boolean isConfirmed = false;

    public SignUpResponseModel(CognitoUser cognitoUser, boolean isConfirmed){
        this.cognitoUser = cognitoUser;
        this.isConfirmed = isConfirmed;
    }

    public CognitoUser getCognitoUser() {
        return cognitoUser;
    }

    public void setCognitoUser(CognitoUser cognitoUser) {
        this.cognitoUser = cognitoUser;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }
}
