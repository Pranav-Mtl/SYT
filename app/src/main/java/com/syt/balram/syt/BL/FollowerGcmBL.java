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
 * Created by Balram on 5/21/2015.
 */
public class FollowerGcmBL
{

        public String SendFollow(String email,String id,String msg)
        {
            String result=getDataStatus(email,id,msg);
            String status=validate(result);
            return status;
        }

        private String getDataStatus(String email,String id,String msg)
        {

            String text = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            String url="email="+email+"&follower_id="+id+"&message="+msg;

            try {
                URI uri = new URI("https", "www.sellyourtime.in", "/api/gcm_followers.php", url, null);
                String ll = uri.toASCIIString();


                System.out.println("gggg" + ll);


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

        public String validate(String strValue)
        {

            System.out.println("Folloowww"+strValue);
            String status="";
            String result=null;

            JSONParser jsonP=new JSONParser();

            try {

                Object obj =jsonP.parse(strValue);


                JSONArray jsonArrayObject = (JSONArray) obj;



                for(int i=0;i<jsonArrayObject.size();i++) {

                    JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                    status=jsonObject.get("status").toString();





                }


                //System.out.println("names"+objSellerFragmentBE.getName());
                //objSellerFragmentBE.setName(arrName);*/



            } catch (Exception e) {



                e.getLocalizedMessage();
            }

            return status;

        }
}
