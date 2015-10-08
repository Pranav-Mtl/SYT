package com.syt.balram.syt.BL;

import android.app.Activity;
import android.os.AsyncTask;

import com.syt.balram.syt.BE.BuyerQuestionBE;
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
 * Created by Balram on 4/15/2015.
 */
public class BuyerQuestionBL {

    Activity context;
    String titleBuyer="Buyer";


    public String insertBuyer(BuyerQuestionBE objBuyerQuestionBE,Activity mContext,String ladooCampaign)
    {



        context=mContext;
        String status="";
        try {

            //String userEmail= Configuration.getSharedPrefrenceValue(mContext,Constant.SHARED_PREFERENCE_UserID);
            String RegisID=Configuration.getSharedPrefrenceValue(mContext, Constant.SHARED_PREFERENCE_RegistrationID);

            if(RegisID==null)
            {
                RegisID="";
            }
            String result =callWS(objBuyerQuestionBE.getEmail(), objBuyerQuestionBE.getPassword(), objBuyerQuestionBE.getFirstname(), objBuyerQuestionBE.getLastname(), objBuyerQuestionBE.getGender(), objBuyerQuestionBE.getBdate(), objBuyerQuestionBE.getPhoneno(), objBuyerQuestionBE.getZip(), objBuyerQuestionBE.getCountry(), titleBuyer, RegisID,objBuyerQuestionBE.getImageURL(),ladooCampaign);
            status=validate(result);
        }
        catch (Exception e)
        {

        }

        return status;
    }

    private class LongRunningGetIO extends AsyncTask<String, String, String> {

       // ProgressDialog progress;

        @Override
        protected void onPreExecute ( )
        {
            //starting the progress dialogue
         /*   progress=new ProgressDialog(context);
           progress.setMessage("Loading...");
            progress.show();
            progress.setCancelable(false);*/

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_BuyerRegister+"email="+params[0]+"&password="+params[1]+"&firstname="+params[2]+"&lastname="+params[3]+"&gender="+params[4]+"&dob="+params[5]+"&phone="+params[6]+"&address="+params[7]+"&zip="+params[8]+"&country="+params[9]+"&title="+titleBuyer);
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

        @Override
        protected void onPostExecute (String result)
        {    //set adapter here
          // progress.dismiss();
            //userName.setText(result);
            super.onPostExecute(result);

        }

          /*  if(result!=null)
            {

                String emailId=validate(result);

                //Toast.makeText(getApplicationContext(), emailId, Toast.LENGTH_LONG).show();

            }
        }
*/

    }

    private String callWS(String email,String password,String firstName,String lastName,String gender,String dob,String phone,String zip,String country,String title,String regID,String imageURL,String paramValue){

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

       // HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_BuyerRegister+"email="+email+"&password="+password+"&firstname="+firstName+"&lastname="+lastName+"&gender="+gender+"&dob="+dob+"&phone="+phone+"&address="+address+"&zip="+zip+"&country="+country+"&title="+title+"&regID="+regID);
        String text = null;
        if(password==null)
        {
            password=" ";
        }
        if(imageURL==null)
        {
            imageURL=" ";
        }

        String url="email="+email+"&password="+password+"&firstname="+firstName+"&lastname="+lastName+"&gender="+gender+"&dob="+dob+"&phone="+phone+"&zip="+zip+"&title="+title+"&regID="+regID+"&image="+imageURL+"&param_value="+paramValue;
        try {

            URI uri = new URI("https", "www.sellyourtime.in", "/api/buyer_register.php",url, null);
            System.out.println("BUYER REGISYTER"+uri);
            String ll=uri.toASCIIString();





            HttpGet httpGet = new HttpGet(ll);
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
