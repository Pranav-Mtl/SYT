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
 * Created by Balram on 6/5/2015.
 */

public class SendOtpBL {

    public String otpSend(String email,String phone,String otp)
    {
        String status=getDataJson(email,phone,otp);
        String result=validate(status);
        return result;
    }
    private String getDataJson(String mail,String phone,String otp)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        String url= Constant.WEBSERVICE_URL+Constant.WEBSERVICE_OTP+"email="+mail+"&phone="+phone+"&otp="+otp;

        HttpGet httpGet = new HttpGet(url);


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
    protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
        InputStream in = entity.getContent();


        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n>0) {
            byte[] b = new byte[4096];
            n =  in.read(b);


            if (n>0) out.append(new String(b, 0, n));
        }


        return out.toString();
    }

    public String validate(String strValue)
    {
        System.out.println("User Profile.."+strValue);


        Long status;
        String result="";

        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);

            JSONArray jsonArrayObject = (JSONArray) obj;

            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());

            result=jsonObject.get("status").toString();



        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return result;

    }
}
