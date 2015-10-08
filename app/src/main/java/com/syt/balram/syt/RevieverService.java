package com.syt.balram.syt;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by Balram on 7/24/2015.
 */
public class RevieverService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String str=intent.getStringExtra("Key");
        String strone=intent.getStringExtra("Value");

        new InsertReferrer().execute(str,strone);

        return super.onStartCommand(intent, flags, startId);
    }

    class InsertReferrer extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            String text = null;
            String url="param_key="+params[0]+"&param_value="+params[1];

            try {
                URI uri = new URI("https", "www.sellyourtime.in", "/api/referrer.php", url, null);
                String ll = uri.toASCIIString();

                System.out.println("Contact URL"+ll);
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


            //return
            return text;
        }
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
