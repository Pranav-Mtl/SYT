package com.syt.balram.syt.BL;

import android.content.Context;

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

/**
 * Created by Balram on 4/10/2015.
 */
public class LoginBL {
    Context mContext;
    public String isUserValid(String userName,String password,String regID,Context context)
    {
        mContext=context;
        String status="";
        try {

            String result =getLoginStatus(userName,password,regID);
            status=validate(result);
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
                String title=jsonObject.get("title").toString();
                Configuration.setSharedPrefrenceValue(mContext,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_LoginTitle,title);
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

    private String getLoginStatus(String email,String password,String regID)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_Login+"email="+email+"&password="+password+"&regID="+regID);
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


}
