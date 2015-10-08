package com.syt.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.syt.constant.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Balram on 4/8/2015.
 */
public class Configuration {


    public static void  setSharedPrefrenceValue(Object objCurrentClassReference, String strPrefenceFileName,
                                                String strKey, String strValue) {

        SharedPreferences sharedPreferencesForStoringData = ((ContextWrapper) objCurrentClassReference)
                .getSharedPreferences(strPrefenceFileName, 0);
        SharedPreferences.Editor sharedPrefencesEditor = sharedPreferencesForStoringData
                .edit();

        //  ArrayList< String> obj=new  ArrayList< String>();
        // LinkedHashSet<String> obj = new LinkedHashSet<String>();


        sharedPrefencesEditor.putString(strKey, strValue);
        //sharedPrefencesEditor.putStringSet("abc", obj);
        sharedPrefencesEditor.commit();


    }




    public static String  getSharedPrefrenceValue(Object objCurrentClassReference,String key) {

        SharedPreferences settings = ((ContextWrapper) objCurrentClassReference)
                .getSharedPreferences(
                        Constant.PREFS_NAME, 1);

        String value=settings.getString(key, null);

        return value;




    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;


        Pattern pattern = Pattern.compile(Constant.STREMAILADDREGEX_FORGOTPASSWORD, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;

    }

    public static boolean isInternetConnection(Context context) {
        // TODO Auto-generated method stub

        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();
        boolean statusInternet;

        if(nf != null && nf.isConnected()==true )
        {
            Log.i("Info:", "Network Available.");
            statusInternet=true;
        }
        else
        {
            Log.i("Info:","Network Not Available.");
            statusInternet=false;

        }
        return statusInternet;
    }
}
