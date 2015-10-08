package com.syt.balram.syt.BL;

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
import java.net.URI;

/**
 * Created by Balram on 5/26/2015.
 */
public class InterestRegisterBL {

    public String insertData(String name,String email,String category,String subCategory,String phone,String description)
    {
        String result=callWS(name,email,category,subCategory,phone,description);
       String status= validate(result);
        return status;
    }

    private String callWS(String name,String email,String category,String subCategory,String phone,String description){

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        //String userID= Configuration.getSharedPrefrenceValue(mcontext, Constant.SHARED_PREFERENCE_UserID);

        String url="name="+name+"&email="+email+"&phone="+phone+"&requirement="+description+"&category="+category+"&subcategory="+subCategory;
       // System.out.println("URL"+url);

        String text = null;
        try {
            URI uri = new URI("https", "www.sellyourtime.in", "/api/category_register.php",url, null);
            String ll=uri.toASCIIString();





            HttpGet httpGet = new HttpGet(ll);

            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);


                HttpEntity entity = response.getEntity();


                text = getASCIIContentFromEntity(entity);


            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
        }catch (IllegalArgumentException e)
        {
            System.out.println("exxmccc"+e);
        }
        catch (Exception e)
        {

        }


        return text;
    }

    public String validate(String strValue)
    {
        System.out.println("jsonnn"+strValue);
        String status="";
        String result=null;

        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);


            JSONArray jsonArrayObject = (JSONArray) obj;

            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());

            status=jsonObject.get("status").toString();



        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return status;

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
