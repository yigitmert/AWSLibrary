package com.softtech.cognitosnslibrary.sns;

public interface OnSnsPostExecute {
    void onSnsSuccess(String snsArnId);
    void onSnsFail();
}
