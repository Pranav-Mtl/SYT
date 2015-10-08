package com.syt.balram.syt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.syt.balram.syt.BE.UserFollowersBE;
import com.syt.balram.syt.BL.FollowerGcmBL;
import com.syt.balram.syt.BL.UserFollowersBL;
import com.syt.constant.Constant;
import com.syt.util.Configuration;


public class UserFollowers extends ActionBarActivity {

    ListView tvFollower;
    UserFollowersBL objUserFollowersBL;
    UserFollowersBE objUserFollowersBE;
    String emailID;
    UserFollowersAdapter objUserFollowersAdapter;
    Button btnSend;
    StringBuffer responseText = new StringBuffer();
    ProgressDialog progressDialog;
    EditText etFollowersMSg;
    FollowerGcmBL objFollowerGcmBL;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_followers);
        back= (ImageButton) findViewById(R.id.user_followers_back);
        tvFollower= (ListView) findViewById(R.id.follower_listview);
        btnSend= (Button) findViewById(R.id.follow_send_btn);
        objUserFollowersBL=new UserFollowersBL();
        objUserFollowersBE=new UserFollowersBE();
        progressDialog=new ProgressDialog(UserFollowers.this);
        etFollowersMSg= (EditText) findViewById(R.id.follower_editText_message);
        objFollowerGcmBL=new FollowerGcmBL();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        emailID= Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.SHARED_PREFERENCE_UserID);

        if(Configuration.isInternetConnection(UserFollowers.this)) {

            new GetFollowerList().execute(emailID);
        }
        else
        {

            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    UserFollowers.this);

// Setting Dialog Title
            alertDialog2.setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);

// Setting Dialog Message
            alertDialog2.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);

// Setting Icon to Dialog


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
                        }
                    });

// Showing Alert Dialog
            alertDialog2.show();
        }


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder sb = new StringBuilder();

                String wordList[]=responseText.toString().split("/");
                for (int i = 0; i < wordList.length; i++) {
                    boolean found = false;
                    for (int j = i+1; j < wordList.length; j++) {
                        if (wordList[j].equals(wordList[i])) {
                            found = true;
                            break;
                        }
                    }
                    // System.out.printf("Checking: [%s]%n", wordList[i]);
                    if (!found) {
                        if (sb.length() > 0)
                            sb.append(',');
                        sb.append(wordList[i]);
                    }
                }
                System.out.println(responseText);
                System.out.println("SVVVV"+sb);

                String msg=etFollowersMSg.getText().toString();

                if(Configuration.isInternetConnection(UserFollowers.this)) {

                    if (msg.length() > 0) {
                        if (sb.length() > 0)
                            new SendGCM().execute(emailID, String.valueOf(sb), msg);
                    }
                }
                else
                {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            UserFollowers.this);

// Setting Dialog Title
                    alertDialog2.setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);

// Setting Dialog Message
                    alertDialog2.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);

// Setting Icon to Dialog


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

                                    dialog.cancel();
                                }
                            });

// Showing Alert Dialog
                    alertDialog2.show();
                }





            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_followers, menu);
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

    public class UserFollowersAdapter extends BaseAdapter{

        Context context;

        public UserFollowersAdapter(Context mContext,String emailID)
        {
            context=mContext;
            objUserFollowersBL.getFollowerList(emailID);

        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public int getCount() {
            return Constant.followFirstNameArray.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.follower_list_raw, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);
                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        //Country country = (Country) cb.getTag();
                        if(cb.isChecked())
                        {
                            //System.out.println("Position"+position);
                            responseText.append(Constant.followIdArray[position]+"/");
                        }
                        /*Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_SHORT).show();*/
                        //country.setSelected(cb.isChecked());
                    }
                });


            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

                holder.code.setText(" (" + Constant.followSubCategoryArray[position] + ")");
                holder.name.setText(Constant.followFirstNameArray[position] + " " + Constant.followLastNameArray[position]);
                holder.name.setChecked(objUserFollowersBE.isSelected());
                holder.name.setTag(objUserFollowersBE);

            convertView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,100));
            return convertView;
        }
    }

    class GetFollowerList extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            objUserFollowersAdapter=new UserFollowersAdapter(getApplicationContext(),params[0]);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            tvFollower.setAdapter(objUserFollowersAdapter);
            progressDialog.dismiss();
        }
    }

    class SendGCM extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... params) {

            String status=objFollowerGcmBL.SendFollow(params[0],params[1],params[2]);
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("1"))
            {
                Toast.makeText(getApplicationContext(),"MSG SEND",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"MSG SEND",Toast.LENGTH_LONG).show();
            }
        }
    }
}
