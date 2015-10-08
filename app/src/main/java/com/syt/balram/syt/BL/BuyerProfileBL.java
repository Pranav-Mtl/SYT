package com.syt.balram.syt.BL;

import com.syt.balram.syt.BE.BuyerProfileBE;
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
 * Created by Balram on 4/29/2015.
 */
public class BuyerProfileBL {

    BuyerProfileBE objBuyerProfileBE;
    String uid;

    public String getBuyerData(BuyerProfileBE buyerProfileBE,String id)
    {
        objBuyerProfileBE=buyerProfileBE;
        uid=id;
        try {
            String result=getDataStatus();
            validate(result);
        }
        catch (Exception E)
        {

        }

        return "";
    }

    public String validate(String strValue) {
        System.out.println("BuyerProfile" + strValue);


        Long status;
        String result = null;

        JSONParser jsonP = new JSONParser();

        try {

            Object obj = jsonP.parse(strValue);


            JSONArray jsonArrayObject = (JSONArray) obj;


            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());

            objBuyerProfileBE.setCategory(jsonObject.get("category").toString());
            objBuyerProfileBE.setSubcategory(jsonObject.get("subcategory").toString());
            objBuyerProfileBE.setTitle(jsonObject.get("syt_title").toString());
            objBuyerProfileBE.setDeliveryDate(jsonObject.get("delievery_date").toString());
            objBuyerProfileBE.setDescription(jsonObject.get("syt_desc").toString());
            objBuyerProfileBE.setDate(jsonObject.get("date").toString());
            objBuyerProfileBE.setPrice(jsonObject.get("price").toString());
            objBuyerProfileBE.setServiceMode(jsonObject.get("service_mode").toString());
            objBuyerProfileBE.setChargeMode(jsonObject.get("charge_mode").toString());
            objBuyerProfileBE.setZip(jsonObject.get("zip").toString());
            objBuyerProfileBE.setFullName(jsonObject.get("fullname").toString());
            objBuyerProfileBE.setExperience(jsonObject.get("experience").toString());
            objBuyerProfileBE.setProfileID(jsonObject.get("login_id").toString());
        }
        catch (Exception e)
        {

        }
        return "";
    }
    private String getDataStatus()
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        System.out.println("URL"+ Constant.WEBSERVICE_URL+Constant.WEBSERVICE_BuyerProfile+"user_id="+uid);
        HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_BuyerProfile+"user_id="+uid);
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
}
