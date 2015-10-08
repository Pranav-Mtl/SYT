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
 * Created by Balram on 5/13/2015.
 */
public class ContactBL {

    public String insertData(String id,String name,String email,String phone,String location,String Detail,String otherProfile)
    {
        String result=getUpdateDataJson(id,name,email,phone,location,Detail,otherProfile);

        String status=validateUpdatedData(result);

        return status;

    }

    private String getUpdateDataJson(String id,String name,String email,String phone,String location,String Detail,String otherProfile)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String text = null;
        String url="userid="+id+"&name="+name+
                "&email="+email+"&phone="+phone+"&location="+location+"&desc="+Detail+"&checked="+otherProfile;

        try {
            URI uri = new URI("https", "www.sellyourtime.in", "/api/connect.php", url, null);
            String ll = uri.toASCIIString();

            System.out.println("Contact URL"+ll);
            HttpGet httpGet = new HttpGet(ll);



            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);


                HttpEntity entity = response.getEntity();


                text = getASCIIContentFromEntity(entity);


            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
        }
        catch (Exception e)
        {

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

    public String validateUpdatedData(String result)
    {

        JSONParser jsonP=new JSONParser();
        String status="";

        try {

            Object obj = jsonP.parse(result);

            JSONArray jsonArrayObject = (JSONArray) obj;

            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());

            status=jsonObject.get("status").toString();
        }
        catch (Exception E)
        {

        }
        return status;
    }
}
