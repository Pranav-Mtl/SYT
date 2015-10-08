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
 * Created by Balram on 4/27/2015.
 */
public class BuyerFragmentBL {

    public String getsellerData()
    {


        String result=getDataStatus();

        String status=validate(result);
        return status;
    }


    private String getDataStatus()
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        System.out.println(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_BuyerFragment);
        HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_BuyerFragment);
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
        System.out.println("Buyer Ticker"+strValue);


        Long status;
        String result=null;

        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);

            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.buyerNameArray=new String[jsonArrayObject.size()];
            Constant.buyerCategoryArray=new String[jsonArrayObject.size()];
            Constant.buyerZipArray=new String[jsonArrayObject.size()];
            Constant.buyerIdArray=new String[jsonArrayObject.size()];
            Constant.buyerSubCategoryArray=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++) {

                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                Constant.buyerNameArray[i]=jsonObject.get("fullname").toString();
                Constant.buyerCategoryArray[i]=jsonObject.get("category").toString();
                Constant.buyerIdArray[i]=jsonObject.get("user_id").toString();
                Constant.buyerZipArray[i]=jsonObject.get("location").toString();
                Constant.buyerSubCategoryArray[i]=jsonObject.get("subcategory").toString();

            }



        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return "";

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

