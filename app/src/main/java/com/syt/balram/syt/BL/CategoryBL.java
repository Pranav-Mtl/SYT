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
 * Created by Balram on 4/30/2015.
 */
public class CategoryBL {

    public String getCategoryData(String title)
    {
        try {
            String result=getDataStatus(title);
            System.out.println("result"+result);
            validate(result);        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return "";
    }


    private String getDataStatus(String mTitle)
    {
        String title=mTitle;
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        System.out.println(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_Category+"categorytitle="+title);

        String url="categorytitle="+title;
        String text = null;

        try {

            URI uri = new URI("https", "www.sellyourtime.in", "/api/categories_count.php", url, null);
            String ll = uri.toASCIIString();
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
    public String getSearchData(String title,String category,String location,String sunCategory)
    {
        String ss="";
        try {
            String result=getSearchDataJson(title,category,location,sunCategory);
            System.out.println("result"+result);
            ss=validate(result);        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return ss;
    }


    private String getSearchDataJson(String mTitle,String mCategory,String mLocation,String mSubCategory)
    {
        String title=mTitle;
        String category=mCategory;
        String Location=mLocation;

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        System.out.println(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_Category+"categorytitle="+title);
        String url="title="+"Seller"+"&category="+category+"&location="+Location+"&subcategory="+mSubCategory;
        String text = null;

        try {

            URI uri = new URI("https", "www.sellyourtime.in", "/api/search.php", url, null);

            System.out.println(uri);
            String ll = uri.toASCIIString();
            HttpGet httpGet = new HttpGet(ll);

            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);


                HttpEntity entity = response.getEntity();


                text = getASCIIContentFromEntity(entity);


            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
        }catch (Exception e)
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

    public String validate(String strValue)
    {
        System.out.println("jsonnn"+strValue);


        String statusJson;

        if(strValue.equals("[]"))
        {
            statusJson="empty";
        }
        else
        {
            statusJson="data";
        }


        Long status;
        String result=null;

        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);


            JSONArray jsonArrayObject = (JSONArray) obj;




            Constant.categoryAvailabilityArray=new String[jsonArrayObject.size()];
            Constant.categoryCategoryArray=new String[jsonArrayObject.size()];
            Constant.categoryExperienceArray=new String[jsonArrayObject.size()];
            Constant.categoryFirstNameArray=new String[jsonArrayObject.size()];
            Constant.categoryIdArray=new String[jsonArrayObject.size()];

            Constant.categoryImageArray=new String[jsonArrayObject.size()];
            Constant.categoryLastNameArray=new String[jsonArrayObject.size()];
            Constant.categorySubCategoryArray=new String[jsonArrayObject.size()];
            Constant.categoryZipArray=new String[jsonArrayObject.size()];
            Constant.categoryPriceMinArray=new String[jsonArrayObject.size()];
            Constant.categoryPriceMaxArray=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++) {

                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.categoryAvailabilityArray[i]=jsonObject.get("service_day").toString();
                Constant.categoryExperienceArray[i]=jsonObject.get("experience").toString();
                Constant.categoryCategoryArray[i]=jsonObject.get("category").toString();
                Constant.categorySubCategoryArray[i]=jsonObject.get("subcategory").toString();

                Constant.categoryFirstNameArray[i]=jsonObject.get("firstname").toString();
                Constant.categoryIdArray[i]=jsonObject.get("id").toString();
                Constant.categoryImageArray[i]=jsonObject.get("image").toString();
                Constant.categoryLastNameArray[i]=jsonObject.get("lastname").toString();

                Constant.categoryZipArray[i]=jsonObject.get("zip").toString();
                Constant.categoryPriceMinArray[i]=jsonObject.get("charge1").toString();
                Constant.categoryPriceMaxArray[i]=jsonObject.get("charge2").toString();




            }


            //System.out.println("names"+objSellerFragmentBE.getName());
            //objSellerFragmentBE.setName(arrName);*/



        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return statusJson;

    }



}
