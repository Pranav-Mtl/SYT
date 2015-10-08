package com.syt.balram.syt.BL;

import com.syt.balram.syt.BE.ManageCategoryBE;
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
 * Created by Balram on 5/8/2015.
 */
public class ManageCategoryBL {

    ManageCategoryBE objManageCategoryBE;



    public String getManageCategoryData(String email,String title,ManageCategoryBE manageCategoryBE)
    {
        objManageCategoryBE=manageCategoryBE;
        String result=getDataJson(email,title);

        System.out.println("categoryResult"+result);
        validate(result);
        return "";
    }
    public String getDataJsonOtherCategory(String email,String title,ManageCategoryBE manageCategoryBE)
    {
        objManageCategoryBE=manageCategoryBE;
        String result=getDataJsonOtherCategory(email,title);

        System.out.println("categoryResult"+result);
        validate(result);
        return "";
    }

    public String updateData(String email,String subCategory,String exp,String expDetails,String availability,String chargeMode,String minPrice,String maxPrice,String location,String category)
    {


        String result=getUpdateDataJson(email, subCategory, exp, expDetails, availability, chargeMode, minPrice, maxPrice, location, category);

        System.out.println("result"+result);

        String status=validateUpdatedData(result);

        System.out.println("status" + status);

        return status;

    }
    private String getDataJson(String mail,String title)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        String url= Constant.WEBSERVICE_URL+Constant.WEBSERVICE_ManageMainCategory+"email="+mail+"&category="+title;

        HttpGet httpGet = new HttpGet(url);

        System.out.println("manage_main_category"+url);


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
    private String getDataJsonOtherCategory(String mail,String title)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        String url= Constant.WEBSERVICE_URL+Constant.WEBSERVICE_ManageCategory+"email="+mail+"&category="+title;

        HttpGet httpGet = new HttpGet(url);




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
    private String getUpdateDataJson(String email,String subCategory,String exp,String expDetails,String availability,String chargeMode,String minPrice,String maxPrice,String location,String category)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String text = null;
        String url="email="+email+"&subcategory="+subCategory+"&experience="+exp+"&experience_details="+expDetails+
                "&service_day="+availability+"&charge_mode="+chargeMode+"&charge1="+minPrice+"&charge2="+maxPrice+"&service_mode="+location+"&category="+category;




        try {
            URI uri = new URI("https", "www.sellyourtime.in", "/api/update_main_category.php", url, null);
            String ll = uri.toASCIIString();

            System.out.println("update_main_category"+ll);
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
        System.out.println("Edit Profile.."+strValue);


        Long status;
        String result=null;

        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);

            JSONArray jsonArrayObject = (JSONArray) obj;

            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
           objManageCategoryBE.setSubcategory(jsonObject.get("subcategory").toString());
            objManageCategoryBE.setExp(jsonObject.get("experience").toString());
            objManageCategoryBE.setExpDetails(jsonObject.get("experience_details").toString());
            objManageCategoryBE.setServiceLocation(jsonObject.get("service_mode").toString());
            objManageCategoryBE.setAvailability(jsonObject.get("service_day").toString());
            objManageCategoryBE.setMinPrice(jsonObject.get("charge1").toString());
            objManageCategoryBE.setMaxPrice(jsonObject.get("charge2").toString());
            objManageCategoryBE.setChargeMode(jsonObject.get("charge_mode").toString());



        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return "";

    }

}
