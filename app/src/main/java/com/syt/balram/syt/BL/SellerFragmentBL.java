package com.syt.balram.syt.BL;

import com.syt.balram.syt.BE.SellerFragmentBE;
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
 * Created by Balram on 4/24/2015.
 */
public class SellerFragmentBL {

    SellerFragmentBE objSellerFragmentBE;
    String arrName[];
    public String getsellerData(SellerFragmentBE sellerFragmentBE)
    {

        objSellerFragmentBE=sellerFragmentBE;
        String result=getDataStatus();
        //System.out.println("result"+result);
        String status=validate(result);
        return status;
    }


    private String getDataStatus()
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        System.out.println(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_SellerFragment);
        HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_SellerFragment);
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
       // System.out.println("jsonnn"+strValue);


        Long status;
        String result=null;

        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);


            JSONArray jsonArrayObject = (JSONArray) obj;


            arrName=new String[jsonArrayObject.size()];

            Constant.sellerNameArray=new String[jsonArrayObject.size()];
            Constant.sellerCategoryArray=new String[jsonArrayObject.size()];
            Constant.sellerZipArray=new String[jsonArrayObject.size()];
            Constant.sellerIdArray=new String[jsonArrayObject.size()];
            Constant.sellerSubCategoryArray=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++) {

                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
              /*  System.out.println("name"+jsonObject.get("name").toString());
                System.out.println("category"+jsonObject.get("category").toString());
                System.out.println("id"+jsonObject.get("id").toString());
                System.out.println("zip"+jsonObject.get("zip").toString());*/
                Constant.sellerNameArray[i]=jsonObject.get("name").toString();
                Constant.sellerCategoryArray[i]=jsonObject.get("category").toString();
                Constant.sellerIdArray[i]=jsonObject.get("id").toString();
                Constant.sellerZipArray[i]=jsonObject.get("zip").toString();
                Constant.sellerSubCategoryArray[i]=jsonObject.get("subcategory").toString();



               /* objSellerFragmentBE.setName(arrName);
                System.out.println("namesArray"+objSellerFragmentBE.getName().toString());*/





            }


            //System.out.println("names"+objSellerFragmentBE.getName());
                //objSellerFragmentBE.setName(arrName);*/



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
