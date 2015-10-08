package com.syt.balram.syt.BL;

import android.content.Context;

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
 * Created by Balram on 6/23/2015.
 */
public class FeedbacKBL {

    Context mContext;
    public String isUserValid(String userName,String message)
    {

        String status="";
        try {

            String result =getLoginStatus(userName,message);
            System.out.println("RESULT"+result);

            status=validate(result);

            System.out.println("STATUS"+status);
        }
        catch (Exception e)
        {

        }
        return status;
    }



    public String validate(String strValue)
    {
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

    private String getLoginStatus(String email,String message)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        String text = null;

            try {
                String url="email="+email+"&message="+message;

                URI uri = new URI("https", "www.sellyourtime.in", "/api/feedback.php", url, null);
                String ll = uri.toASCIIString();


                System.out.println("Feedback" + ll);
                HttpGet httpGet = new HttpGet(ll);
            HttpResponse response = httpClient.execute(httpGet, localContext);


            HttpEntity entity = response.getEntity();


            text = getASCIIContentFromEntity(entity);


        } catch (Exception e) {
            return e.getLocalizedMessage();
        }


        return text;

    }

}
