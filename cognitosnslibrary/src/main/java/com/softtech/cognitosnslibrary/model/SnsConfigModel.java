package com.softtech.cognitosnslibrary.model;

public class SnsConfigModel {

    private String snsPlatformApplicationArn = null;
    private String cognitoIdentityPoolId = null;
    private String regionStringValue = "cognito-idp.eu-central-1.amazonaws.com";

    public SnsConfigModel(String snsPlatformApplicationArn, String cognitoIdentityPoolId, String regionStringValue){
        this.snsPlatformApplicationArn = snsPlatformApplicationArn;
        this.cognitoIdentityPoolId = cognitoIdentityPoolId;
        this.regionStringValue = regionStringValue;
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
