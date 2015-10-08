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
 * Created by pranav mittal on 4/10/2015.
 * purpose: to check email available or not for signup.
 * result: if available we get
 *              status=1
 *          else
 *              status=0
 *
 */
public class SignUpValidity {
    String emailId;

    public String isEmailAvailable(String email)
    {
        try {

            String result = getData(email);
            emailId=validate(result);
        }
        catch (Exception e)
        {

        }
        return emailId;
    }


    public String validate(String strValue)
    {
        System.out.println("Hssoos"+strValue);
        Long status;
        String result=null;

        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(strValue);

            JSONArray jsonArrayObject = (JSONArray) obj;

            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());

            status=(Long) jsonObject.get("status");

            if(status==1)
            {
                result="y";
            }
            else
            {
                Constant.SocialMediaLoginTitle=jsonObject.get("title").toString();
                result="n";

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
        while (n>0) {
            byte[] b = new byte[4096];
            n =  in.read(b);


            if (n>0) out.append(new String(b, 0, n));
        }


        return out.toString();
    }

    private String getData(String email)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        System.out.println(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_Register+"email="+email);
        HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_Register+"email="+email);
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

    public String isSocialEmailAvailable(String email,String regID)
    {
        try {

            String result = getSocialData(email, regID);
            emailId=validateSocial(result);
        }
        catch (Exception e)
        {

        }
        return emailId;
    }


    public String validateSocial(String strValue)
    {
        System.out.println("Hssoos"+strValue);
        Long status;
        String result=null;

        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(strValue);

            JSONArray jsonArrayObject = (JSONArray) obj;

            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());

            status=(Long) jsonObject.get("status");

            if(status==1)
            {
                Constant.SocialMediaLoginTitle=jsonObject.get("title").toString();
                result="n";
            }
            else
            {

                result="y";

            }


        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return result;

    }



    private String getSocialData(String email,String regID)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        System.out.println(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_SocialLogin+"email="+email+"&regID="+regID);
        HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_SocialLogin+"email="+email+"&regID="+regID);
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
