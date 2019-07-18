package com.softtech.cognitosnslibrary.signup;

import android.content.Context;
import android.os.Bundle;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.softtech.cognitosnslibrary.model.CognitoConfigModel;
import com.softtech.cognitosnslibrary.model.SignUpInfoModel;
import com.softtech.cognitosnslibrary.model.SignUpResponseModel;
import com.softtech.cognitosnslibrary.util.CognitoUtil;

public class SignUpProcess {

    private static SignUpProcess signUpProcess;

    private Context context = null;
    private SignUpInfoModel signUpInfoModel = null;
    private CognitoConfigModel cognitoConfigModel = null;
    private OnSignUpPostExecute onSignUpPostExecute = null;

    private SignUpProcess(Context context, SignUpInfoModel signUpInfoModel, CognitoConfigModel cognitoConfigModel, OnSignUpPostExecute onSignUpPostExecute){
        this.context = context;
        this.signUpInfoModel = signUpInfoModel;
        this.cognitoConfigModel = cognitoConfigModel;
        this.onSignUpPostExecute = onSignUpPostExecute;
    }

    public static SignUpProcess init(Context context, SignUpInfoModel signUpInfoModel, CognitoConfigModel cognitoConfigModel, OnSignUpPostExecute onSignUpPostExecute){
        if (signUpProcess == null){
            synchronized (SignUpProcess.class){
                if (signUpProcess == null){
                    signUpProcess = new SignUpProcess(context, signUpInfoModel, cognitoConfigModel, onSignUpPostExecute);
                }
            }
        }
        return signUpProcess;
    }

    public void signUp(){
        CognitoUtil.setPool(context, cognitoConfigModel);

        CognitoUserAttributes userAttributes = new CognitoUserAttributes();
        if (signUpInfoModel.getFullPhoneNumber() != null){
            userAttributes.addAttribute("phone_number", signUpInfoModel.getFullPhoneNumber());
        }
        if (signUpInfoModel.getEmail() != null){
            userAttributes.addAttribute("email", signUpInfoModel.getEmail());
        }
        if (signUpInfoModel.getName() != null){
            userAttributes.addAttribute("name", signUpInfoModel.getName());
        }

        CognitoUtil.getPool().signUpInBackground(signUpInfoModel.getUserId(), signUpInfoModel.getPassword(), userAttributes, null, new SignUpHandler() {

            @Override
            public void onSuccess(CognitoUser cognitoUser, boolean userConfirmed, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                onSignUpPostExecute.onSignUpSuccess(new SignUpResponseModel(cognitoUser, userConfirmed));
            }

            @Override
            public void onFailure(Exception exception) {
                onSignUpPostExecute.onSignUpFail(exception);
            }
        });
    }

    public void confirmSignUp(String verificationCode){
        CognitoUtil.getPool().getUser(signUpInfoModel.getFullPhoneNumber().substring(1)).confirmSignUpInBackground(verificationCode, true, new GenericHandler() {

            @Override
            public void onSuccess() {
                onSignUpPostExecute.onSignUpSuccess(new SignUpResponseModel(null, true));
            }

            @Override
            public void onFailure(Exception exception) {
                onSignUpPostExecute.onSignUpFail(exception);
            }
        });
    }

}
