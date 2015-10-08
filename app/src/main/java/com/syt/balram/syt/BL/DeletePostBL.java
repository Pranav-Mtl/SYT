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
 * Created by Balram on 6/30/2015.
 */
public class DeletePostBL {

    public String postData(String postID)
    {
        String result=getUpdateDataJson(postID);

        String status=validateUpdatedData(result);

        return status;

    }

    private String getUpdateDataJson(String id)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String text = null;
        String url="post_id="+id;

        try {
            URI uri = new URI("https", "www.sellyourtime.in", "/api/delete_post.php", url, null);
            String ll = uri.toASCIIString();

            System.out.println("DELETE URL"+ll);
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
        System.out.println("DELETE RESULT"+result);

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
