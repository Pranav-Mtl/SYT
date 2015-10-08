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
 * Created by Balram on 5/30/2015.
 */
public class MyPostsBL {
    public String getsellerData(String email)
    {


        String result=getDataStatus(email);

        String status=validate(result);
        return status;
    }


    private String getDataStatus(String email)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        System.out.println(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_MyPosts);
        HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_MyPosts+"email="+email);
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
        System.out.println("My Post Ticker"+strValue);
        String result="";
        if(strValue.equals("[]"))
        {
            result="NO";
            return result;
        }
        else
        {
            result="YES";
        }


        Long status;


        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);

            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.myPostCategory=new String[jsonArrayObject.size()];
            Constant.myPostSubCategory=new String[jsonArrayObject.size()];
            Constant.myPostId=new String[jsonArrayObject.size()];
            Constant.myPostDate=new String[jsonArrayObject.size()];
            Constant.myPostTitle=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++) {

                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                Constant.myPostCategory[i]=jsonObject.get("category").toString();
                Constant.myPostSubCategory[i]=jsonObject.get("subcategory").toString();
                Constant.myPostId[i]=jsonObject.get("id").toString();
                Constant.myPostDate[i]=jsonObject.get("date").toString();
                Constant.myPostTitle[i]=jsonObject.get("syt_title").toString();

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
