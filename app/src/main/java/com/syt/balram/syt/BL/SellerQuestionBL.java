package com.syt.balram.syt.BL;

import android.content.Context;

import com.syt.balram.syt.BE.SellerQuestionBE;
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
 * Created by Balram on 4/22/2015.
 */
public class SellerQuestionBL {

    Context context;
    SellerQuestionBE objSellerQuestionBE;
    String regID;

    public String insertSeller(SellerQuestionBE sellerQuestionBE,Context mContext,String ladooCampaign)
    {
        objSellerQuestionBE=sellerQuestionBE;
        context=mContext;
        regID= Configuration.getSharedPrefrenceValue(context, Constant.SHARED_PREFERENCE_RegistrationID);

        if(regID==null)
        {
            regID="";
        }



        String result=callWS(ladooCampaign);
        String status=validate(result);

        return status;
    }


    private String callWS(String ladooCampaign){

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        if(objSellerQuestionBE.getPassword()==null)
        {
            objSellerQuestionBE.setPassword("");
        }
        if(objSellerQuestionBE.getImageURL()==null)
        {
            objSellerQuestionBE.setImageURL("");
        }

        String url="fn="+objSellerQuestionBE.getFirstName()+"&ln="+objSellerQuestionBE.getLastName()+
                "&email="+objSellerQuestionBE.getEmail()+"&password="+objSellerQuestionBE.getPassword()+"&gender="+objSellerQuestionBE.getGender()+
                "&dob="+objSellerQuestionBE.getDob()+"&individual="+objSellerQuestionBE.getServicesWhom()+"&desc="+objSellerQuestionBE.getDescription()+
                "&travel="+objSellerQuestionBE.getDistance()+"&phone="+objSellerQuestionBE.getPhone()+
                "&zip="+objSellerQuestionBE.getZip()+"&chargemode="+objSellerQuestionBE.getServiceCharge()+"&minprice="+objSellerQuestionBE.getMinPrice()+
                "&maxprice="+objSellerQuestionBE.getMaxPrice()+"&serviceday="+objSellerQuestionBE.getServicesWhen()+"&notified="+objSellerQuestionBE.getServiceNotified()+
                "&category="+objSellerQuestionBE.getCategory()+"&subcategory="+objSellerQuestionBE.getSubCategory()+"&exp="+objSellerQuestionBE.getExperience()+
                "&title="+"Seller"+"&servicemode="+objSellerQuestionBE.getServicesWhere()+"&regID="+regID+"&image="+objSellerQuestionBE.getImageURL()+"&param_value="+ladooCampaign;


        String text = null;

        try {
            URI uri = new URI("https", "www.sellyourtime.in", "/api/seller_register.php",url, null);
            String ll=uri.toASCIIString();


            System.out.println("gggg"+ll);



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
        System.out.println("jsonnn"+strValue);
        String status="";
        String result=null;

        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);


            JSONArray jsonArrayObject = (JSONArray) obj;

            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());

            status=jsonObject.get("status").toString();



        } catch (Exception e) {



            e.getLocalizedMessage();
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


}
