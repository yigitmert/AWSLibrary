package com.softtech.cognitosnslibrary.sns;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;
import com.amazonaws.regions.Region;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.GetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.GetEndpointAttributesResult;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.NotFoundException;
import com.amazonaws.services.sns.model.SetEndpointAttributesRequest;
import com.softtech.cognitosnslibrary.model.CognitoConfigModel;
import com.softtech.cognitosnslibrary.util.CognitoUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SnsProcess {

    private static SnsProcess snsProcess;

    private Context context = null;
    private CognitoConfigModel cognitoConfigModel = null;
    private OnSnsPostExecute onSnsPostExecute = null;

    private String jwtToken = null;
    private String firebaseInstanceId = null;

    private AmazonSNSClient snsClient = null;
    private String arnStorage = null;

    private SnsProcess(Context context, String jwtToken, String firebaseInstanceId, CognitoConfigModel cognitoConfigModel, OnSnsPostExecute onSnsPostExecute){
        this.context = context;
        this.jwtToken = jwtToken;
        this.firebaseInstanceId = firebaseInstanceId;
        this.cognitoConfigModel = cognitoConfigModel;
        this.onSnsPostExecute = onSnsPostExecute;
    }

    public static SnsProcess init(Context context, String jwtToken, String firebaseInstanceId, CognitoConfigModel cognitoConfigModel, OnSnsPostExecute onSnsPostExecute){
        if (snsProcess == null){
            synchronized (SnsProcess.class){
                if (snsProcess == null){
                    snsProcess = new SnsProcess(context, jwtToken, firebaseInstanceId, cognitoConfigModel, onSnsPostExecute);
                }
            }
        }
        return snsProcess;
    }

    public void createEndpointForSns() {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(context, cognitoConfigModel.getCognitoIdentityPoolId(), cognitoConfigModel.getCognitoRegion());

        Map<String, String> logins = new HashMap<String, String>();
        logins.put(cognitoConfigModel.getRegionStringValue() + "/" + cognitoConfigModel.getUserPoolId(), jwtToken);
        credentialsProvider.setLogins(logins);

        snsClient = new AmazonSNSClient(credentialsProvider);
        snsClient.setRegion(Region.getRegion(cognitoConfigModel.getCognitoRegion()));

        AsyncTask<String, Void, String> mTask = new AsyncTask<String, Void, String> () {

            @Override
            protected String doInBackground(String... params) {
                registerWithSNS();
                return "OK";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
            }
        };
        mTask.execute();
    }

    private void registerWithSNS() {

        boolean updateNeeded = false;
        boolean createNeeded = (null == arnStorage);

        if (createNeeded) {
            arnStorage = createEndpoint(firebaseInstanceId);
            createNeeded = false;
        }

        try {
            GetEndpointAttributesRequest geaReq = new GetEndpointAttributesRequest().withEndpointArn(arnStorage);
            GetEndpointAttributesResult geaRes = snsClient.getEndpointAttributes(geaReq);

            updateNeeded = !geaRes.getAttributes().get("Token").equals(firebaseInstanceId) || !geaRes.getAttributes().get("Enabled").equalsIgnoreCase("true");

        } catch (NotFoundException nfe) {
            createNeeded = true;
        }

        if (createNeeded) {
            arnStorage = createEndpoint(firebaseInstanceId);
        }

        System.out.println("updateNeeded = " + updateNeeded);

        if (updateNeeded) {
            System.out.println("Updating platform endpoint " + arnStorage);
            Map attribs = new HashMap();
            attribs.put("Token", firebaseInstanceId);
            attribs.put("Enabled", "true");
            SetEndpointAttributesRequest saeReq = new SetEndpointAttributesRequest()
                    .withEndpointArn(arnStorage)
                    .withAttributes(attribs);
            snsClient.setEndpointAttributes(saeReq);
        }

        CognitoUserAttributes attributes = new CognitoUserAttributes();
        attributes.addAttribute("custom:endpointarn", (arnStorage));

        CognitoUtil.getPool().getCurrentUser().updateAttributesInBackground(attributes, new UpdateAttributesHandler() {
            @Override
            public void onSuccess(List<CognitoUserCodeDeliveryDetails> attributesVerificationList) {
                onSnsPostExecute.onSnsSuccess(arnStorage);
            }

            @Override
            public void onFailure(Exception exception) {
                onSnsPostExecute.onSnsFail();
            }
        });
    }

    private String createEndpoint(String token) {

        String endpointArn = null;
        try {
            Map<String, String> endpointAtt = new HashMap<String, String>();
            endpointAtt.put("UserId", CognitoUtil.getCurrSession().getUsername());
            Log.d("******", "CognitoUtil.getCurrSession().getUsername() : " + CognitoUtil.getCurrSession().getUsername());
            CreatePlatformEndpointRequest cpeReq = new CreatePlatformEndpointRequest()
                    .withPlatformApplicationArn(cognitoConfigModel.getSnsPlatformApplicationArn())
                    .withToken(token)
                    .withAttributes(endpointAtt);
            CreatePlatformEndpointResult cpeRes = snsClient.createPlatformEndpoint(cpeReq);
            endpointArn = cpeRes.getEndpointArn();
        } catch (InvalidParameterException ipe) {
            String message = ipe.getErrorMessage();
            if (message.contains("already exists with the same Token, but different attributes.")){

            }
            Pattern p = Pattern.compile(".*Endpoint (arn:aws:sns[^ ]+) already exists " + "with the same Token.*");
            Matcher m = p.matcher(message);
            if (m.matches()) {
                endpointArn = m.group(1);
            } else {
                throw ipe;
            }
        }

        return endpointArn;
    }

}
