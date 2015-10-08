package com.syt.balram.syt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;


public class FollowerMessage extends ActionBarActivity {

    TextView etName,etMsg;
    Button btnProfile;
    String id,msg,name;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_message);

        etName= (TextView) findViewById(R.id.followed_name);
        etMsg= (TextView) findViewById(R.id.followed_mesage);
        btnProfile= (Button) findViewById(R.id.followed_profile_btn);
        back= (ImageButton) findViewById(R.id.follower_message_back);

        Intent intent=getIntent();
        Bundle bb=intent.getExtras();
        String json=bb.get("JSON").toString();

        try {
            JSONObject jsonObj = new JSONObject(json);
            id=jsonObj.getString("id");
            msg=jsonObj.getString("greetMsg");
            name=jsonObj.getString("fullname");

            etName.setText("You have new message from "+name);
            etMsg.setText("Message: "+msg);

        }
        catch (Exception e)
        {

        }

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getApplicationContext(),SellerProfile.class);
                intent1.putExtra("ID",id);
                startActivity(intent1);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_follower_message, menu);
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
