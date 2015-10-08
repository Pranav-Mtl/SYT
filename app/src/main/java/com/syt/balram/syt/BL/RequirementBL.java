package com.syt.balram.syt.BL;

import android.content.Context;

import com.syt.balram.syt.BE.RequirementBE;
import com.syt.constant.Constant;
import com.syt.util.Configuration;

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
 * Created by Balram on 4/24/2015.
 */
public class RequirementBL {


    Context mcontext;
    RequirementBE objRequirementBE;

    public String registerRequirement(RequirementBE requirementBE,Context context)
    {
        objRequirementBE=requirementBE;
        mcontext=context;
        String result=callWS();
        String status=validate(result);
        return status;
    }


    private String callWS(){

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String userID= Configuration.getSharedPrefrenceValue(mcontext, Constant.SHARED_PREFERENCE_UserID);

        String url="category="+objRequirementBE.getCategory()+"&subcategory="+objRequirementBE.getSubCategory()+
                "&serviceday="+objRequirementBE.getServiceDays()+"&exp="+objRequirementBE.getExperience()+
                "&servicemode="+objRequirementBE.getServiceLocation()+"&chargemode="+objRequirementBE.getServiceCharge()+"&minprice="+objRequirementBE.getMinPrice()+
                "&maxprice="+objRequirementBE.getMaxPrice()+"&notif="+objRequirementBE.getServiceContacted()+"&title="+objRequirementBE.getTitle()+
                "&email="+userID+"&desc="+objRequirementBE.getDesc();

       // System.out.println("URLlll"+url);

        String text = null;
        try {
            URI uri = new URI("https", "www.sellyourtime.in", "/api/post_requirement.php",url, null);
            String ll=uri.toASCIIString();

            System.out.println(uri);
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
        System.out.println("Requirement summmartyy"+strValue);
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

}
