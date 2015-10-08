package com.syt.balram.syt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.syt.balram.syt.BL.ChangePasswordBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;
import com.google.android.gms.analytics.HitBuilders;


public class ChangePassword extends ActionBarActivity {

    EditText etCurrent,etNew,etConfirm;
    Button btnChange;
    ChangePasswordBL objChangePasswordBL;
    TransparentProgressDialog progressDialog;
    String userEmail;
    AlertDialog alertDialog;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etCurrent= (EditText) findViewById(R.id.change_current_pswd);
        etNew= (EditText) findViewById(R.id.change_new_pswd);
        etConfirm= (EditText) findViewById(R.id.change_confirm_pswd);
        btnChange= (Button) findViewById(R.id.change_btn);
        back= (ImageButton) findViewById(R.id.change_back);
        objChangePasswordBL=new ChangePasswordBL();
        progressDialog=new TransparentProgressDialog(ChangePassword.this,R.drawable.logo_single);
        userEmail= Configuration.getSharedPrefrenceValue(ChangePassword.this, Constant.SHARED_PREFERENCE_UserID);
        alertDialog = new AlertDialog.Builder(ChangePassword.this).create();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textCurrent=etCurrent.getText().toString();
                String textNew=etNew.getText().toString();
                String textConfirm=etConfirm.getText().toString();

                if(textCurrent.trim().length()==0)
                {
                    etCurrent.setError("required");
                }
               else if(textNew.trim().length()==0)
                {
                    etNew.setError("required");
                }
                else if(textConfirm.trim().length()==0)
                {
                    etConfirm.setError("required");
                }
                else
                {
                    if(textNew.equals(textConfirm))
                    {
                        if(Configuration.isInternetConnection(ChangePassword.this)) {
                            new LongRunningGetIO().execute(userEmail, textCurrent, textNew);
                        }
                        else{

                            alertDialog
                                    .setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);
                            alertDialog.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);
                            alertDialog.setButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            //finish();
                                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                                        }
                                    });

                            alertDialog.show();

                        }
                    }
                    else
                    {
                        etConfirm.setError("password mismatch");
                    }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class LongRunningGetIO extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

           String result= objChangePasswordBL.isUserValid(params[0],params[1],params[2]);
           return result;
        }

        @Override
        protected void onPostExecute(String s) {

            progressDialog.dismiss();
            if(s.equals("y"))
            {
                MyApp.tracker().setScreenName("Change Password Screen");
                MyApp.tracker().send(new HitBuilders.EventBuilder("Button", "Click")
                        .setCategory("UI")
                        .setAction("Click")
                        .setLabel("Submit")
                        .build());
                Toast.makeText(ChangePassword.this,"Password Changed Successfully",Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                etCurrent.setError("wrong password");
            }

        }
    }
}
