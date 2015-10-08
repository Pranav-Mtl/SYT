package com.syt.balram.syt.BL;

import com.syt.balram.syt.BE.EditProfileBE;
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
 * Created by Balram on 5/7/2015.
 */
public class EditProfileBL {


    EditProfileBE objEditProfileBE;
    String emailID;

    public String getData(String email,EditProfileBE editProfileBE)
    {
        objEditProfileBE=editProfileBE;
        emailID=email;

        String result=getDataJson(emailID);
        validate(result);

        return "";

    }

    public String updateData(String email,String fname,String lname,String zip,String dob,String phone,EditProfileBE editProfileBE)
    {
        objEditProfileBE=editProfileBE;

        String result=getUpdateDataJson(email,fname,lname,zip,dob,phone);

        System.out.println("result"+result);

        String status=validateUpdatedData(result);

        System.out.println("status"+status);

        return status;
    }

    private String getDataJson(String mail)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        String url= Constant.WEBSERVICE_URL+Constant.WEBSERVICE_EditProfile+"email="+mail;

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

    private String getUpdateDataJson(String email,String fname,String lname,String zip,String dob,String phone)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String text = null;
        String url="email="+email+"&firstname="+fname+
                "&lastname="+lname+"&zip="+zip+"&dob="+dob+"&phone="+phone;

        try {
            URI uri = new URI("https", "www.sellyourtime.in", "/api/edit_profile.php", url, null);
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
            objEditProfileBE.setFirstName(jsonObject.get("firstname").toString());
            objEditProfileBE.setLastname(jsonObject.get("lastname").toString());
            //objEditProfileBE.setAddress(jsonObject.get("address").toString());
            objEditProfileBE.setZip(jsonObject.get("zip").toString());
            objEditProfileBE.setDob(jsonObject.get("dob").toString());
            objEditProfileBE.setPhone(jsonObject.get("phone").toString());
            objEditProfileBE.setCategoryMain(jsonObject.get("category_main").toString());
            objEditProfileBE.setCategory(jsonObject.get("category").toString());



        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return "";

    }
}
