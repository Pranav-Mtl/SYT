package com.syt.balram.syt.BL;

import com.syt.constant.Constant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Balram on 4/23/2015.
 */
public class ForgotPasswordBL {

    public String isUserValid(String userName, String password) {
        String status = "";
        try {

            String result = getLoginStatus(userName, password);
            System.out.println("Result"+result);
            status = validate(result);
        } catch (Exception e) {

        }
        return status;
    }


    public String validate(String strValue) {
        Long status;
        String result = null;

        JSONParser jsonP = new JSONParser();

        try {

            Object obj = jsonP.parse(strValue);


            JSONArray jsonArrayObject = (JSONArray) obj;

            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());

            status = (Long) jsonObject.get("status");

            if (status == 1) {
                result = "y";
            } else {
                result = "n";

            }


        } catch (Exception e) {


            e.getLocalizedMessage();
        }

        return result;

    }

    protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
        InputStream in = entity.getContent();


        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n > 0) {
            byte[] b = new byte[4096];
            n = in.read(b);


            if (n > 0) out.append(new String(b, 0, n));
        }


        return out.toString();
    }

    private String getLoginStatus(String email, String password) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        System.out.println("Forget URL"+Constant.WEBSERVICE_URL + Constant.WEBSERVICE_ForgotPassword + "email=" + email + "&password=" + password);

        HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL + Constant.WEBSERVICE_ForgotPassword + "email=" + email + "&password=" + password);
        String text = null;
        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);


            HttpEntity entity = response.getEntity();


            text = getASCIIContentFromEntity(entity);


        } catch (Exception e) {
            return e.getLocalizedMessage();
        }


        return text;

    }

    public String sendVerification(String email,String code)
    {
        String result=sendCodeJson(email,code);
        String status=validate(result);
        return status;
    }

    private String sendCodeJson(String email, String code) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        System.out.println("Forget URL"+Constant.WEBSERVICE_URL + Constant.WEBSERVICE_ForgotPasswordCode + "email=" + email + "&code=" + code);

        HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL + Constant.WEBSERVICE_ForgotPasswordCode + "email=" + email + "&code=" + code);
        String text = null;
        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);


            HttpEntity entity = response.getEntity();


            text = getASCIIContentFromEntity(entity);


        } catch (Exception e) {
            return e.getLocalizedMessage();
        }


        return text;

    }


}


