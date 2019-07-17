package com.softtech.cognitosnslibrary.login;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.softtech.cognitosnslibrary.model.LoginSnsResponseModel;

public interface OnLoginPostExecute {
    void onLoginSuccess(LoginSnsResponseModel loginSnsResponseModel);
    void onMfaCodeRequested(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation);
    void onLoginFail(Exception exception);
}
