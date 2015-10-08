package com.syt.balram.syt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import com.syt.balram.syt.BL.ForgotPasswordBL;
import com.syt.constant.Constant;
import com.syt.util.Configuration;


public class NewPassword extends ActionBarActivity {
    EditText etCode,etNewPassword,etConfPassword;
    Button btnSet;
    String emailid;
    ForgotPasswordBL objForgotPasswordBL;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        etCode= (EditText) findViewById(R.id.newpassword_verification);
        etNewPassword= (EditText) findViewById(R.id.newpassword_pswd);
        etConfPassword= (EditText) findViewById(R.id.newpassword_confpswd);
        btnSet= (Button) findViewById(R.id.newpassword_btn);
        back= (ImageButton) findViewById(R.id.new_password_back);
        objForgotPasswordBL=new ForgotPasswordBL();
        alertDialog = new AlertDialog.Builder(NewPassword.this).create();

        progressDialog=new ProgressDialog(NewPassword.this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent=getIntent();
        Bundle bb=intent.getExtras();
         emailid=bb.get("UserName").toString();

        System.out.println(emailid);



        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=etCode.getText().toString();
                String newPassword=etNewPassword.getText().toString();
                String confPassword=etConfPassword.getText().toString();

                if(code.trim().length()==0)
                {
                    etCode.setError("required");
                }
                else
                {
                    if(code.equals(Constant.userOTP))
                    {
                        if(!(newPassword.trim().length()==0 || confPassword.trim().length()==0)) {
                            if (newPassword.equals(confPassword)) {

                                System.out.println("ffff"+emailid+newPassword);
                                if(Configuration.isInternetConnection(NewPassword.this)) {

                                    new LongRunningGetIO().execute(emailid, newPassword);
                                }
                                else{

                                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                            NewPassword.this);

// Setting Dialog Title
                                    alertDialog2.setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);

// Setting Dialog Message
                                    alertDialog2.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);

// Setting Icon to Dialog

                                    alertDialog2.setIcon(R.drawable.no_internet);
// Setting Positive "Yes" Btn
                                    alertDialog2.setPositiveButton("YES",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Write your code here to execute after dialog
                                                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                                                }
                                            });
// Setting Negative "NO" Btn
                                    alertDialog2.setNegativeButton("NO",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Write your code here to execute after dialog
                                                    Toast.makeText(getApplicationContext(),
                                                            "You clicked on NO", Toast.LENGTH_SHORT)
                                                            .show();
                                                    dialog.cancel();
                                                    finish();
                                                }
                                            });

// Showing Alert Dialog
                                    alertDialog2.show();


                                }

                            } else {

                                etConfPassword.setError("Password Mismatch");
                            }
                        }
                        else {
                            etNewPassword.setError("required");
                            etConfPassword.setError("required");
                        }
                    }
                    else
                    {
                        etCode.setError("Incorrect code");
                    }
                }
            }
        });
    }


    private class LongRunningGetIO extends AsyncTask<String, String, String> {

        // ProgressDialog progress;

        @Override
        protected void onPreExecute ( )
        {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

           String result= objForgotPasswordBL.isUserValid(params[0],params[1]);

          //  String result=objSellerQuestionBL.insertSeller(objSellerQuestionBE);

            return result;

        }

        @Override
        protected void onPostExecute (String result)
        {    //set adapter here
            progressDialog.dismiss();


            if(result.equals("y"))
            {
                Toast.makeText(NewPassword.this,"Password Updated",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(NewPassword.this,LoginScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(NewPassword.this,"Problem with password updation",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(NewPassword.this,RegisterScreen.class);
                startActivity(intent);

            }
            //userName.setText(result);

            //super.onPostExecute(result);

        }

          /*  if(result!=null)
            {

                String emailId=validate(result);

                //Toast.makeText(getApplicationContext(), emailId, Toast.LENGTH_LONG).show();

            }
        }
*/

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_password, menu);
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
}
