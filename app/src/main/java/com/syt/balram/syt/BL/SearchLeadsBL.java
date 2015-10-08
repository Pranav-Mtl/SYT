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
import java.net.URI;

/**
 * Created by Balram on 5/30/2015.
 */
public class SearchLeadsBL {

    public String getsellerData(String category,String location)
    {


        String result=getDataStatus(category,location);

        String status=validate(result);
        return status;
    }


    private String getDataStatus(String cat,String loc)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        System.out.println(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_SearchPost);
        //HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_SearchPost);
        String text = null;
        String url="category="+cat+"&location="+loc;
        try {

            URI uri = new URI("https", "www.sellyourtime.in", "/api/search_post.php",url, null);
            String ll=uri.toASCIIString();
            HttpGet httpGet=new HttpGet(ll);
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

            Constant.SearchNameArray=new String[jsonArrayObject.size()];
            Constant.SearchCategoryArray=new String[jsonArrayObject.size()];
            Constant.SearchZipArray=new String[jsonArrayObject.size()];
            Constant.SearchIdArray=new String[jsonArrayObject.size()];
            Constant.SearchSubCategoryArray=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++) {

                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                Constant.SearchNameArray[i]=jsonObject.get("fullname").toString();
                Constant.SearchCategoryArray[i]=jsonObject.get("category").toString();
                Constant.SearchIdArray[i]=jsonObject.get("id").toString();
                Constant.SearchZipArray[i]=jsonObject.get("location").toString();
                Constant.SearchSubCategoryArray[i]=jsonObject.get("subcategory").toString();

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
