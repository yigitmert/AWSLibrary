package com.softtech.cognitosnslibrary.login;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoServiceConstants;
import com.softtech.cognitosnslibrary.model.CognitoConfigModel;
import com.softtech.cognitosnslibrary.model.LoginSnsResponseModel;
import com.softtech.cognitosnslibrary.sns.OnSnsPostExecute;
import com.softtech.cognitosnslibrary.sns.SnsProcess;
import com.softtech.cognitosnslibrary.util.CognitoUtil;

import java.util.HashMap;
import java.util.Locale;

public class LoginProcess {

    private static LoginProcess loginProcess;

    private Context context = null;
    private String userName = null;
    private String password = null;
    private String firebaseInstanceId = null;
    private OnLoginPostExecute onLoginPostExecute = null;
    private CognitoConfigModel cognitoConfigModel = null;

    private LoginProcess(Context context, String userName, String password, String firebaseInstanceId, CognitoConfigModel cognitoConfigModel, OnLoginPostExecute onLoginPostExecute){
        this.context = context;
        this.userName = userName;
        this.password = password;
        this.firebaseInstanceId = firebaseInstanceId;
        this.cognitoConfigModel = cognitoConfigModel;
        this.onLoginPostExecute = onLoginPostExecute;
    }

    public static LoginProcess init(Context context, String userName, String password, String firebaseInstanceId, CognitoConfigModel cognitoConfigModel, OnLoginPostExecute onLoginPostExecute){
        if (loginProcess == null){
            synchronized (LoginProcess.class){
                if (loginProcess == null){
                    loginProcess = new LoginProcess(context, userName, password, firebaseInstanceId, cognitoConfigModel, onLoginPostExecute);
                }
            }
        }
        return loginProcess;
    }

    public void onVerifyMfaCode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation, String mfaCode) {
        multiFactorAuthenticationContinuation.setMfaCode(mfaCode);
        multiFactorAuthenticationContinuation.continueTask();
    }

    public void login(){
        CognitoUtil.setPool(context, cognitoConfigModel);
        CognitoUtil.setUser(userName);
        CognitoUtil.getPool().getUser(userName).signOut();
        CognitoUtil.getPool().getUser(userName).getSessionInBackground(authenticationHandler);
    }

    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(final CognitoUserSession cognitoUserSession, CognitoDevice device) {
            CognitoUtil.setCurrSession(cognitoUserSession);
            CognitoUtil.newDevice(device);

            if (cognitoConfigModel.getSnsConfigModel() != null){
                SnsProcess snsProcess = SnsProcess.init(context, cognitoUserSession.getIdToken().getJWTToken(), firebaseInstanceId, cognitoConfigModel, new OnSnsPostExecute() {
                    @Override
                    public void onSnsSuccess(String snsArnId) {
                        onLoginPostExecute.onLoginSuccess(new LoginSnsResponseModel(cognitoUserSession, snsArnId));
                    }

                    @Override
                    public void onSnsFail() {
                        onLoginPostExecute.onLoginSuccess(new LoginSnsResponseModel(cognitoUserSession, null));
                    }
                });
                snsProcess.createEndpointForSns();
            }
            else{
                onLoginPostExecute.onLoginSuccess(new LoginSnsResponseModel(cognitoUserSession, null));
            }
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            Locale.setDefault(Locale.US);
            getUserAuthentication(authenticationContinuation);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            onLoginPostExecute.onMfaCodeRequested(multiFactorAuthenticationContinuation);
        }

        @Override
        public void onFailure(final Exception e) {
            onLoginPostExecute.onLoginFail(e);
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            continuation.setChallengeResponse(CognitoServiceConstants.CHLG_RESP_ANSWER, password);
            continuation.continueTask();
        }
    };

    private void getUserAuthentication(AuthenticationContinuation continuation) {
        AuthenticationDetails authenticationDetails = null;
        if (cognitoConfigModel.isCustomAuthMode()){
            authenticationDetails = new AuthenticationDetails(userName, new HashMap<String, String>(), new HashMap<String, String>());
        }
        else{
            authenticationDetails = new AuthenticationDetails(userName, password, new HashMap<String, String>());
        }

        authenticationDetails.setAuthenticationParameter(CognitoServiceConstants.AUTH_PARAM_USERNAME, userName);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

}
