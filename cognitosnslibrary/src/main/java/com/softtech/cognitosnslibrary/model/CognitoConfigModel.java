package com.softtech.cognitosnslibrary.model;

import com.amazonaws.regions.Regions;

public class CognitoConfigModel {

    private boolean isCustomAuthMode = false;

    private String userPoolId = null;
    private String clientId = null;
    private String clientSecret = null;
    private Regions cognitoRegion = null;
    private String snsPlatformApplicationArn = null;
    private String cognitoIdentityPoolId = null;
    private String regionStringValue = "cognito-idp.eu-central-1.amazonaws.com";

    private boolean isCreateSnsArnId = false;

    public CognitoConfigModel(String userPoolId, String clientId, String clientSecret, Regions cognitoRegion, boolean isCustomAuthMode){
        this.userPoolId = userPoolId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.cognitoRegion = cognitoRegion;
        this.isCustomAuthMode = isCustomAuthMode;
    }

    public String getUserPoolId() {
        return userPoolId;
    }

    public void setUserPoolId(String userPoolId) {
        this.userPoolId = userPoolId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Regions getCognitoRegion() {
        return cognitoRegion;
    }

    public void setCognitoRegion(Regions cognitoRegion) {
        this.cognitoRegion = cognitoRegion;
    }

    public boolean isCreateSnsArnId() {
        return isCreateSnsArnId;
    }

    public void setCreateSnsArnId(boolean createSnsArnId) {
        isCreateSnsArnId = createSnsArnId;
    }

    public boolean isCustomAuthMode() {
        return isCustomAuthMode;
    }

    public void setCustomAuthMode(boolean customAuthMode) {
        isCustomAuthMode = customAuthMode;
    }

    public String getSnsPlatformApplicationArn() {
        return snsPlatformApplicationArn;
    }

    public void setSnsPlatformApplicationArn(String snsPlatformApplicationArn) {
        this.snsPlatformApplicationArn = snsPlatformApplicationArn;
    }

    public String getCognitoIdentityPoolId() {
        return cognitoIdentityPoolId;
    }

    public void setCognitoIdentityPoolId(String cognitoIdentityPoolId) {
        this.cognitoIdentityPoolId = cognitoIdentityPoolId;
    }

    public String getRegionStringValue() {
        return regionStringValue;
    }

    public void setRegionStringValue(String regionStringValue) {
        this.regionStringValue = regionStringValue;
    }
}
