package com.syt.balram.syt;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.syt.constant.Constant;
import com.syt.util.Configuration;

import java.net.URLDecoder;



/**
 * Created by Balram on 7/24/2015.
 */
public	class	InstallReferrerReceiver	extends BroadcastReceiver {
    @Override
    public	void	onReceive(Context context,	Intent intent)	{

       //	the	referrer	params	is	passed	via	the	intent
       // new CampaignTrackingReceiver().onReceive(context, intent);

        Log.w("Referrer", "Inside referrer class");
        //Toast.makeText(context,"REFERRER",Toast.LENGTH_LONG).show();
        String	rawReferrer	=intent.getStringExtra("referrer");

        Log.w("Referrer",rawReferrer);


        if	(rawReferrer	!=	null)	{
            String	referrers[]	=
                    intent.getStringExtra("referrer").split("&");
            for	(String	referrerValue	:	referrers)	{
                String	keyValue[]	=
                        referrerValue.split("=");
// the	key,	tracking_id	in	this	case,	is
                //	stored	in	param_key
                String	param_key	=URLDecoder.decode(keyValue[0]);
// the	tracking	id	value	(i.e.	12345)	is
                // stored	in	param_value



                String	param_value	=
                        URLDecoder.decode(keyValue[1]);
                //	do	something	with
                //	the	param	value
                System.out.println("KEY" + param_key);
                System.out.println("VALUE" + param_value);

                Log.d("Key", param_key);
                Log.d("Value",param_value);


               /* Toast.makeText(context,"KEY"+param_key,Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"VALUE"+param_key,Toast.LENGTH_SHORT).show();*/


                if(true) {
                    //System.out.println("Yes Ladoo LADOOO");
                    Configuration.setSharedPrefrenceValue(context, Constant.PREFS_NAME, Constant.CampaignLadoo, param_value);
                    Intent intent1 = new Intent(context, RevieverService.class);
                    intent1.putExtra("Key", param_key);
                    intent1.putExtra("Value", param_value);
                    context.startService(intent1);
                }

                //new InsertReferrer().execute(param_key,param_value);
            }
        }
        //
    }

}
