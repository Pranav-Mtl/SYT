package com.syt.balram.syt.BL;

import com.syt.balram.syt.BE.ProfileBE;
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
 * Created by Balram on 4/10/2015.
 */
public class ProfileBL {

    ProfileBE profileBE;

    public String getProfileData(String email ,ProfileBE objProfileBE)
    {
        profileBE=objProfileBE;
        String status="";
        try {
            String result =getProfileJson(email);
            status = validate(result);
        }
        catch(Exception e) {
        }
        return status;
    }

    private String getProfileJson(String email)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_Profile+"email="+email);
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




    public String validate(String strValue)
    {
        System.out.println("Profile JSON"+strValue);
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
                result="done";
                profileBE.setFirstName((String)jsonObject.get("firstname"));
                profileBE.setLastName((String) jsonObject.get("lastname"));
                profileBE.setCategory((String) jsonObject.get("category"));
                profileBE.setSubCategory((String) jsonObject.get("subcategory"));
                profileBE.setPicture((String) jsonObject.get("image"));
                profileBE.setId(jsonObject.get("id").toString());
                profileBE.setAppURL(jsonObject.get("app_url").toString());

            }
            else
            {
                result="notDone";

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


}
