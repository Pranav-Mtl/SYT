package com.syt.balram.syt.BL;

import com.syt.balram.syt.BE.SellerProfileBE;
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
 * Created by Balram on 4/27/2015.
 */
public class SellerProfileBL {

    SellerProfileBE objSellerProfileBE;
    String uid,emailId;


    public String getsellerData(SellerProfileBE sellerProfileBE,String id,String email)
    {

        System.out.println("ID"+id);
        objSellerProfileBE=sellerProfileBE;
        uid=id;
        emailId=email;
        String result=getDataStatus();
        System.out.println("result"+result);
        String status=validate(result);
        System.out.println("status"+status);
        return status;
    }

    private String getDataStatus()
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        System.out.println("URL"+Constant.WEBSERVICE_URL+Constant.WEBSERVICE_SellerProfile+"id="+uid+"&email="+emailId);
        HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_SellerProfile+"id="+uid+"&email="+emailId);
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
        System.out.println("jsonnn"+strValue);


        Long status;
        String result="";

        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);


            JSONArray jsonArrayObject = (JSONArray) obj;




                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());

                objSellerProfileBE.setFirstName(jsonObject.get("firstname").toString());
                objSellerProfileBE.setLastName(jsonObject.get("lastname").toString());
                objSellerProfileBE.setCategoryName(jsonObject.get("category").toString());
                objSellerProfileBE.setSubCategoryName(jsonObject.get("subcategory").toString());
                objSellerProfileBE.setPicURL(jsonObject.get("image").toString());
                objSellerProfileBE.setExperience(jsonObject.get("experience").toString());
                objSellerProfileBE.setServiceMode(jsonObject.get("charge_mode").toString());
                objSellerProfileBE.setServiceCharge(jsonObject.get("charge").toString());
                objSellerProfileBE.setDeliveryLocation(jsonObject.get("service_mode").toString());
                objSellerProfileBE.setLocation(jsonObject.get("zip").toString());
                objSellerProfileBE.setAvailabity(jsonObject.get("service_day").toString());
                objSellerProfileBE.setExpDetails(jsonObject.get("experience_details").toString());
                objSellerProfileBE.setRating(jsonObject.get("rate").toString());
                objSellerProfileBE.setFollowType(jsonObject.get("follow_type").toString());
                result=jsonObject.get("status").toString();
           /* System.out.println(jsonObject.get("firstname").toString());
            System.out.println(jsonObject.get("lastname").toString());
            System.out.println(jsonObject.get("category").toString());
            System.out.println(jsonObject.get("subcategory").toString());
            System.out.println(jsonObject.get("image").toString());
            System.out.println("experienec"+jsonObject.get("experience").toString());
            System.out.println("charge Mode"+jsonObject.get("charge_mode").toString());
            System.out.println(jsonObject.get("charge").toString());
            System.out.println(jsonObject.get("service_mode").toString());
            System.out.println(jsonObject.get("zip").toString());
            System.out.println(jsonObject.get("service_day").toString());
            System.out.println(jsonObject.get("experience_details").toString());
            System.out.println(jsonObject.get("rate").toString());*/


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
