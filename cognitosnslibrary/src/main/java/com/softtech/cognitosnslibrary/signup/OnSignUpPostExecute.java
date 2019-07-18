package com.softtech.cognitosnslibrary.signup;

import com.softtech.cognitosnslibrary.model.SignUpResponseModel;

public interface OnSignUpPostExecute {
    void onSignUpSuccess(SignUpResponseModel signUpResponseModel);
    void onSignUpFail(Exception ex);
}
