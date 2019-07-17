package com.softtech.cognitosnslibrary.model;

import com.amazonaws.regions.Regions;

public class CognitoConfigModel {

    private boolean isCustomAuthMode = false;

    private String userPoolId = null;
    private String clientId = null;
    private String clientSecret = null;
    private Regions cognitoRegion = null;
    private SnsConfigModel snsConfigModel = null;

    public CognitoConfigModel(String userPoolId, String clientId, String clientSecret, Regions cognitoRegion, boolean isCustomAuthMode, SnsConfigModel snsConfigModel){
        this.userPoolId = userPoolId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.cognitoRegion = cognitoRegion;
        this.isCustomAuthMode = isCustomAuthMode;
        this.snsConfigModel = snsConfigModel;
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

    public boolean isCustomAuthMode() {
        return isCustomAuthMode;
    }

    public void setCustomAuthMode(boolean customAuthMode) {
        isCustomAuthMode = customAuthMode;
    }

    public SnsConfigModel getSnsConfigModel() {
        return snsConfigModel;
    }

    public void setSnsConfigModel(SnsConfigModel snsConfigModel) {
        this.snsConfigModel = snsConfigModel;
    }
}
