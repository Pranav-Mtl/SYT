package com.syt.balram.syt.BL;

import com.syt.balram.syt.BE.UserProfileBE;
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
 * Created by Balram on 5/7/2015.
 */
public class UserProfileBL {

    UserProfileBE objUserProfileBE;
    String emailID;

    public String getData(String email,UserProfileBE userProfileBE)
    {
        objUserProfileBE=userProfileBE;
        emailID=email;
        String result=getDataJson(emailID);
        validate(result);

        return "";

    }

    private String getDataJson(String mail)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        String url= Constant.WEBSERVICE_URL+Constant.WEBSERVICE_UserProfile+"email="+mail;

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
        String result=null;

        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);

            JSONArray jsonArrayObject = (JSONArray) obj;

            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());

            objUserProfileBE.setFirstName(jsonObject.get("firstname").toString());
            objUserProfileBE.setLastName(jsonObject.get("lastname").toString());
            objUserProfileBE.setSubCategory(jsonObject.get("subcategory").toString());
            objUserProfileBE.setCategory(jsonObject.get("category").toString());
            objUserProfileBE.setAddress(jsonObject.get("zip").toString());
            objUserProfileBE.setDetails(jsonObject.get("experience_details").toString());
            objUserProfileBE.setDob(jsonObject.get("dob").toString());
            objUserProfileBE.setImage(jsonObject.get("image").toString());
            objUserProfileBE.setPhone(jsonObject.get("phone").toString());
            objUserProfileBE.setLeads(jsonObject.get("lead").toString());
            //objUserProfileBE.setZip(jsonObject.get("zip").toString());



        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return "";

    }
}
