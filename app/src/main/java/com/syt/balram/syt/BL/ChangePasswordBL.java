package com.syt.balram.syt.BL;

/**
 * Created by Balram on 4/28/2015.
 */

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
public class ChangePasswordBL {


    public String isUserValid(String email,String oldpass,String confirmpass)
    {
        String status="";
        try {
            String result=getLoginStatus(email,oldpass,confirmpass);
            status=validate(result);
        }
        catch(Exception e)
        {
            System.out.println("in first catch block");
            e.printStackTrace();
        }
        return status;
    }


    public String validate(String strValue)
    {
        System.out.println("the value of strValue===  "+strValue);
        Long status;
        String result=null;
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=(Long)jsonObject.get("status");
            if(status==1)
            {
                result="y";
            }
            else
            {
                result="n";
            }
        } catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
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
    private String getLoginStatus(String email,String oldpassword,String newPass)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_ChangePassword+"email="+email+"&oldpassword="+oldpassword+"&newpassword="+newPass);
        String text = null;
        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            text = getASCIIContentFromEntity(entity);
        } catch (Exception e) {
            System.out.println("in last catch block");
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return text;
    }
}
